<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/utils.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
<title>积分管理</title>
<link rel="stylesheet" type="text/css"
	href="${webSource}/app/static/css/style.css">
<script type="text/javascript">
	//声明积分类型变量
	var sort = 1;
	
	//声明滚动加载标识
	var scrollFlag = true;
	
	//获得积分事件
	function achieveScore() {
		//调整页面样式
		$("#plus").addClass("hover");
		$("#plus").addClass("points-active");
		$("#minus").removeClass("hover");
		$("#minus").removeClass("points-active");

		$("#achieveScore").html('');
		$("#consumeScore").html('');

		$("#pageNum").val(1);

		//加载积分流水列表
		sort = 1;
		showScoreList();
	}

	//消费积分事件
	function consumeScore() {
		//调整页面样式
		$("#minus").addClass("hover");
		$("#minus").addClass("points-active");
		$("#plus").removeClass("hover");
		$("#plus").removeClass("points-active");

		$("#achieveScore").html('');
		$("#consumeScore").html('');

		$("#pageNum").val(1);

		//加载积分流水列表
		sort = 2;
		showScoreList();
	}

	//加载积分流水
	function showScoreList() {
		//显示loading
		$("#loading").show();

		//设置页码加+1
		var pageNum = parseInt($("#pageNum").val());
		$("#pageNum").val(pageNum + 1);

		//ajax请求积分流水
		$.ajax({
			type : "POST",
			url : "${basePath}service/h5_jfScoreDetail",
			data : {
				"pageNum" : pageNum,
				"sort" : sort
			},
			success : function(result) {
				//隐藏loading 
				$("#loading").hide();
				
				//用户不存在
				if (result.code == 10022) {
					//TODO:需要客户端重新登录
					return;
				}
				
				//请求失败
				if (result.code != 0) {
					gotoShowToast(result.message);
					return;
				}

				//无数据
				if (null == result.data.scoreList
						|| result.data.scoreList.length <= 0) {
					if (pageNum == 1) {
						$("#nodata").show();
					} else {
						//TODO:显示【已经全部加载完毕】
						
						//重置滚动状态
						scrollFlag = false;
					}
					return;
				}

				//显示列表
				var content = "";
				$.each(result.data.scoreList, function(i, item) {
					content += '<div class="points1 points-active">';
					content += '<div class="points-content">';
					content += '<div class="points-left">';
					if (sort == 1) {
						content += '<span>+' + item.fvalue + '</span>';
					} else {
						content += '<span>' + item.fvalue + '</span>';
					}
					content += '<em>' + item.sintegralRuleName + '</em> ';
					content += '</div><div class="points-right">';
					content += item.taddTime + '</div></div></div>';
				});
				if (sort == 1) {//获得积分列表
					$("#achieveScore").append(content);
					$("#achieveScore").show();
					$("#consumeScore").hide();
				} else {//消费积分列表
					$("#consumeScore").append(content);
					$("#consumeScore").show();
					$("#achieveScore").hide();
				}
				
				//重置滚动状态
				scrollFlag = true;
			}
		});
	}

	//页面滚动刷新
	var dheight = $(document.body).outerHeight(true);
	var wdheight = $(window).height();
	var height = dheight - wdheight;
	var scrollTop = $(document).scrollTop();
	$(window).scroll(function() {
		dheight = $(document.body).outerHeight(true);
		wdheight = $(window).height();
		height = dheight - wdheight;
		scrollTop = $(document).scrollTop();
		if (scrollTop >= height && scrollFlag == true) {
			showScoreList();
		}
	});

	//逛商城事件
	function gotoJfhome() {
		//window.location.href = "${basePath}service/h5_jfhome";
		window.history.go(-1);
	}

	//页面加载完成，加载【获得积分】数据的操作
	$(function() {
		achieveScore();
	});
</script>
</head>
<body>
	<div class="container uncontainer">
		<!-- 顶部切换tab -->
		<div class="points">
			<ul>
				<li id="plus" onclick="achieveScore();">获得积分</li>
				<li id="minus" onclick="consumeScore();">消费积分</li>
			</ul>
		</div>

		<!-- 获得积分 -->
		<div class="points-main" id="achieveScore"></div>

		<!-- 消费积分 -->
		<div class="points1" id="consumeScore"></div>

		<!-- loading -->
		<div
			style="margin: 10px auto; height: 24px; text-align: center; display: none">
			<img id="loading" src="${webSource}/app/static/img/center/load.gif">
		</div>

		<!-- 暂无数据 -->
		<div id="nodata" style="display: none;">
			<img style="margin: 30px 45% 0; width: 10%;"
				src="${webSource}/app/static/img/center/notik.png" title="暂无数据">
			<p
				style="text-align: center; line-height: 40px; color: #9b9b9b; font-size: 12px;">暂无数据</p>
		</div>

		<!-- 顶部悬浮视图 -->
		<div class="points-position">
			<div class="points-sun">
				<div>
					<img src="${webSource}/app/static/img/points.png" /> 我的积分：${score }
					<a href="javascript:gotoJfhome();">逛商城</a>
				</div>
			</div>
		</div>

		<!-- 隐藏域 -->
		<input type="hidden" id="pageNum" value="1">
	</div>
</body>
</html>