package com.znjf.app.web.module.h5;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.tziba.model.jf.Pointcommodity;
import com.tziba.model.zx.Advertis;
import com.tziba.tdp.utils.RedisCacheInfoUtil;
import com.tziba.zookeeper.utils.EvnUtils;
import com.znjf.app.model.jf.service.PointcommodityService;
import com.znjf.app.web.common.base.ZNJFBaseController;

/**
 * 中南金服App服务端-积分商城
 * */
@Controller
@RequestMapping("/service")
public class ZNJFScoreMallController extends ZNJFBaseController {

	@Autowired
	private PointcommodityService pointcommodityService;
	
	/**
	 * 积分商城-首页
	 * */
	@RequestMapping("/h5_jfhome")
	public String jfhome(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			//获取图片路径
			String imageUrlPrex = EvnUtils.getValue("static.http.domain");
			request.setAttribute("imageUrlPrex", imageUrlPrex);
			
			//获取滚动页
			Object bannerObj = redisCached.getHashCached("tzb_app_index", "app_jf_banner");
			List<Advertis> banners = JSON.parseArray(bannerObj.toString(), Advertis.class);
			request.setAttribute("banners", banners);
			
			//获取登录标识与用户积分
			request.setAttribute("flag", false);
			request.setAttribute("score", 100);
			
			//获取九宫格商品
			Object productListObj = redisCached.getHashCached("tzb_app_index", "app_jf_products");
			List<Advertis> productList = JSON.parseArray(productListObj.toString(), Advertis.class);
			request.setAttribute("productList", productList);
			
			//获取推荐商品
			List<Pointcommodity> recommendList = pointcommodityService.selectRecommendCommodity(new Page<Pointcommodity>(1, 3));
			request.setAttribute("recomendList", recommendList);
			
			return "jfCenter/jfIndex";
		} catch (Exception e) {
			//跳转异常捕获页面
			return "error/404";
		}
	}
}
