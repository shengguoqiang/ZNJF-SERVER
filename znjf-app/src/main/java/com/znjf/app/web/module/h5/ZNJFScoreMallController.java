package com.znjf.app.web.module.h5;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.tziba.model.hd.IntegralSerial;
import com.tziba.model.hy.IntegralAccount;
import com.tziba.model.jf.Pointcommodity;
import com.tziba.model.zx.Advertis;
import com.tziba.tdp.utils.RedisCacheInfoUtil;
import com.tziba.zookeeper.utils.EvnUtils;
import com.znjf.app.model.hd.service.IntegralSerialService;
import com.znjf.app.model.hy.service.IntegralAccountService;
import com.znjf.app.model.jf.service.PointcommodityService;
import com.znjf.app.web.common.base.ZNJFBaseController;
import com.znjf.app.web.common.base.ZNJFConstants;
import com.znjf.app.web.common.base.ZNJFResponseResult;
import com.znjf.app.web.common.utils.ZNJFSafeMap;
import com.znjf.app.web.common.utils.ZNJFUtils;

/**
 * 中南金服App服务端-积分商城
 */
@Controller
@RequestMapping("/service")
public class ZNJFScoreMallController extends ZNJFBaseController {

	@Autowired
	private PointcommodityService pointcommodityService;

	@Autowired
	private IntegralAccountService integralAccountService;

	@Autowired
	private IntegralSerialService integralSerialService;

	/**
	 * 积分商城-首页
	 */
	@RequestMapping("/h5_jfhome")
	public String jfhome(HttpServletRequest request, HttpServletResponse response) {

		try {
			// 获取图片路径
			String imageUrlPrex = EvnUtils.getValue("static.http.domain");
			request.setAttribute("imageUrlPrex", imageUrlPrex);

			// 获取滚动页
			Object bannerObj = redisCached.getHashCached("tzb_app_index", "app_jf_banner");
			List<Advertis> banners = JSON.parseArray(bannerObj.toString(), Advertis.class);
			request.setAttribute("banners", banners);

			// 获取登录标识与用户积分
			request.setAttribute("flag", false);
			request.setAttribute("score", 100);

			// 获取九宫格商品
			Object productListObj = redisCached.getHashCached("tzb_app_index", "app_jf_products");
			List<Advertis> productList = JSON.parseArray(productListObj.toString(), Advertis.class);
			request.setAttribute("productList", productList);

			// 获取推荐商品
			List<Pointcommodity> recommendList = pointcommodityService
					.selectRecommendCommodity(new Page<Pointcommodity>(1, 3));
			request.setAttribute("recomendList", recommendList);

			return "jfCenter/jfIndex";
		} catch (Exception e) {
			// 跳转异常捕获页面
			return "error/404";
		}
	}

	/**
	 * 积分商城-我的积分
	 */
	@RequestMapping("/h5_jfScore")
	public String jfscore(HttpServletRequest request, HttpServletResponse response) {

		try {
			// Cookie中获取token,并据此从Redis中查询用户ID
			String token = ZNJFUtils.readCookie(request, ZNJFConstants.COOKIE_TOKEN_KEY);
			String uid = (String) redisCached.getCached(token);
			// 查询用户的积分账户
			IntegralAccount integralAccount = new IntegralAccount();
			integralAccount.setSuserId(uid);
			List<IntegralAccount> accounts = integralAccountService.selectByEntityWhere(integralAccount);
			if (CollectionUtils.isNotEmpty(accounts)) {
				integralAccount = accounts.get(0);
			}
			request.setAttribute("score", integralAccount.getIusablePoints());

			return "jfCenter/jfScore";
		} catch (Exception e) {
			log.error("h5_jfScore接口异常：" + e);
			return "error/404";
		}
	}

	/**
	 * 积分商城-积分详情
	 */
	@RequestMapping("/h5_jfScoreDetail")
	@ResponseBody
	public Object jfScoreDetail(HttpServletRequest request, HttpServletResponse response) {
		ZNJFSafeMap resMap = new ZNJFSafeMap();
		ZNJFSafeMap dataMap = new ZNJFSafeMap();
		try {
			// 获取参数
			String token = ZNJFUtils.readCookie(request, ZNJFConstants.COOKIE_TOKEN_KEY);
			String uid = (String) redisCached.getCached(token);
			if (StringUtils.isBlank(uid)) {
				return ZNJFUtils.buildResult("h5_jfScoreDetail", resMap,
						ZNJFResponseResult.ResultEnum.USER_NOTEXIST.getCode(),
						ZNJFResponseResult.ResultEnum.USER_NOTEXIST.getDesc());
			}
			String pageNum = request.getParameter("pageNum");
			String sort = request.getParameter("sort");

			// 查询积分流水库
			ZNJFSafeMap paramMap = new ZNJFSafeMap();
			paramMap.put("suserId", uid);
			paramMap.put("sort", sort);
			paramMap.put("orderCondition", "TADD_TIME");
			paramMap.put("queryCondition", "desc");
			List<IntegralSerial> scoreList = integralSerialService
					.selectScoreListBySort(new Page<IntegralSerial>(Integer.parseInt(pageNum), 10), paramMap);
			dataMap.put("scoreList", scoreList);
			return ZNJFUtils.buildResult("h5_jfScoreDetail", resMap, dataMap,
					ZNJFResponseResult.ResultEnum.SUCCESS.getCode(), ZNJFResponseResult.ResultEnum.SUCCESS.getDesc());
		} catch (Exception e) {
			log.error("h5_jfScoreDetail接口异常：" + e);
			return ZNJFUtils.buildResult("h5_jfScoreDetail", resMap,
					ZNJFResponseResult.ResultEnum.SERVICE_ERROR.getCode(),
					ZNJFResponseResult.ResultEnum.SERVICE_ERROR.getDesc());
		}
	}
}
