<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/utils.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
<title>积分商城</title>
<link rel="stylesheet" type="text/css"
	href="${webSource}/app/static/css/mall.css" />
<link rel="stylesheet" type="text/css"
	href="${webSource}/app/static/css/header.css" />
<script src="${webSource}/app/static/js/touchslider.js"
	type="text/javascript" charset="utf-8"></script>
</head>
<body>
	<div class="wap_content">
		<!-- 商品轮播图 -->
		<div class="mall_loop">
			<div class="loop_image swipe">
				<ul id="loop_ul">
					<c:if test="${not empty banners }">
						<c:forEach items="${banners }" var="banner" varStatus="status">
							<li class="loopli"><img class="looplim"
								alt="${banner.sourceTitle }"
								src="${imageUrlPrex }${banner.contentUrl }"></li>
						</c:forEach>
					</c:if>
				</ul>
			</div>
		</div>
		<c:if test="${not empty banners }">
			<ul id="loop_colum">
				<c:forEach items="${banners }" varStatus="status">
					<c:if test="${status.index == 0 }">
						<li class="loop_li loop_lion"></li>
					</c:if>
					<c:if test="${status.index != 0 }">
						<li class="loop_li"></li>
					</c:if>
				</c:forEach>
			</ul>
		</c:if>
		
		<!-- 积分-订单-地址 -->
		<div class="mall_tab">
			<div class="tab_class" id ="mySocre" value="${flag }" onclick="gotoScoreCenter();">
				<span><img src="${webSource}/wap/static/newimg/mall/mall-jf.jpg" /></span>
				<c:if test="${flag == false }">
					我的积分
				</c:if>
				<c:if test="${flag == true }">
					${score}&nbsp;积分
				</c:if>
			</div>
			<div class="tab_class" onclick="myOrderList();">
				<span ><img src="${webSource}/wap/static/newimg/mall/adr-dd.jpg" /></span>订单管理
			</div>
			<div class="tab_class" onclick="addressManager();">
				<span><img src="${webSource}/wap/static/newimg/mall/mall-adr.jpg" /></span>地址管理
			</div>
		</div>
		
		<!-- 商品九宫格 -->
		<div class="mall_banner">
			<c:if test="${not empty productList }" >
				<c:forEach varStatus="status" var="pro"  items="${productList }">
					<c:if  test="${status.index == 0 }">
						<div class="banner_left">
							<p class="banner_title">${pro.title }</p>
							<span class="banner_tip">${pro.remark }</span>
							<img class="left_goods" onclick="goToMore('${pro.href}');" src="${imageUrlPrex }${pro.contentUrl }" />
						</div>
					</c:if>
					<div class="banner_right">
						<c:if  test="${status.index == 1 }">
							<div class="banner_top">
							<p class="banner_title">${pro.title }</p>
							<span class="banner_tip">${pro.remark }</span>
								<img class="top_goods" onclick="goToMore('${pro.href}');" src="${imageUrlPrex }${pro.contentUrl }" />
							</div>
						</c:if>
						<c:if  test="${status.index == 2}">
							<div class="banner_bot">
							<p class="banner_title">${pro.title }</p>
							<span class="banner_tip">${pro.remark }</span>
								<img class="bot_goods" onclick="goToMore('${pro.href}');" src="${imageUrlPrex }${pro.contentUrl }" />
							</div>
						</c:if>
					</div>
				</c:forEach>
			</c:if>
		</div>
		
		<!-- 推荐商品 -->
		<div class="mall_title">推荐商品<a class="mall_more"  onclick="goToList();">更多...</a></div>
		<ul class="mall_list">
			<c:if test="${not empty recomendList }">
			<c:forEach items="${recomendList }" var="rec">
				<li class="mall_li" onclick="goToDetail('${rec.id}');">
				<img class="li_icon" src="${imageUrlPrex }<tziba:imageSize size="180x150" srcPath="${rec.scommoditypath }"/>" />
				<div class="li_content">
					<div class="li_detail">
						<p class="detail_name">
							<c:if test="${rec.scommodityname !=null }">
									<c:choose>
										<c:when
											test="${fn:length(rec.scommodityname) > 20 }">
											<c:out value="${fn:substring(rec.scommodityname, 0, 17) }..." />
										</c:when>
										<c:otherwise>
											<c:out value="${rec.scommodityname }" />
										</c:otherwise>
									</c:choose>
							</c:if>  
						</p>
						<p class="detail_tip">
						<c:if test="${rec.sbrief !=null }">
									<c:choose>
										<c:when test="${fn:length(rec.sbrief) > 50 }">
											<c:out value="${fn:substring(rec.sbrief, 0, 47) }..." />
										</c:when>
										<c:otherwise>
											<c:out value="${rec.sbrief }" />
										</c:otherwise>
									</c:choose>
							</c:if>  
						</p>
						<p class="detail_num"><span><img src="${webSource}/wap/static/newimg/mall/goodsicon.jpg" /></span>${rec.fwantpoint }<span class="numall">已兑${rec.fcash !=null ? rec.fcash : 0 }件</span></p>
					</div>
				</div>
			</li>
			</c:forEach>
			</c:if>
		</ul>
		
	</div>
</body>
<script type="text/javascript">
		//banner轮播
		var as = document.getElementById('loop_colum')
				.getElementsByTagName('li');
		var loop = new TouchSlider('loop_ul', {
			speed : 1000,
			'direction' : '0',
			interval : 5000,
			fullsize : true
		});
		loop.play();
		loop.on('before', function(m, n) {
			as[m].className = 'loop_li';
			as[n].className = 'loop_li loop_lion';
		});
		
		//查看我的积分
		function gotoScoreCenter() {
			if($("#mySocre").attr("value") == true){
				alert("已登录");
			}else{
				var token = "";
				if (browser.versions.android) {
					token = window.tziba.isLogin();
				} else if (browser.versions.iPhone || browser.versions.ios) {
					ZNJFEventHandler.callNativeFunction("judgeLogin", null, "judgeLoginCallBack", function(token){
						if (token == "" || token == null) {
							//未登录逻辑处理！
							alert("未登录");
						} else {
							//已登录逻辑处理！
							alert("已登录：" + token);
							window.location.href = "${basePath}service/h5_jfScore";
						}
					});
				}
			}
		}
		
	</script>
</html>