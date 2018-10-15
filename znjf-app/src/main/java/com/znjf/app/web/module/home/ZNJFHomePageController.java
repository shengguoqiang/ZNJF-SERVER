package com.znjf.app.web.module.home;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.tziba.model.app.BussinessConfig;
import com.tziba.model.zx.Advertis;
import com.tziba.model.zx.Article;
import com.tziba.model.zx.Navigation;
import com.tziba.model.zy.ProjectBasicInfo;
import com.tziba.tdp.utils.GrpParaUtil;
import com.znjf.app.model.app.service.BussinessConfigService;
import com.znjf.app.model.hy.service.BaseInfoService;
import com.znjf.app.model.zx.service.ArticleService;
import com.znjf.app.model.zx.service.NavigationService;
import com.znjf.app.model.zy.service.ProjectBasicInfoService;
import com.znjf.app.web.common.base.ZNJFBaseController;
import com.znjf.app.web.common.base.ZNJFResponseResult;
import com.znjf.app.web.common.utils.ZNJFSafeMap;
import com.znjf.app.web.common.utils.ZNJFUtils;

/**
 * 中南金服App服务端-首页模块
 * */
@Controller
@RequestMapping("/service")
public class ZNJFHomePageController extends ZNJFBaseController {

	@Autowired
	private NavigationService navigationService;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private BussinessConfigService bussinessConfigService;
	
	@Autowired
	private BaseInfoService baseInfoService;
	
	@Autowired
	private ProjectBasicInfoService projectBasicInfoService;
	 
	/**
	 * 首页
	 * @author dqs
	 * */
	@RequestMapping("/homePage")
	@ResponseBody
	public Object homePage(HttpServletRequest request, HttpServletResponse response) {
		ZNJFSafeMap resMap = new ZNJFSafeMap();
		ZNJFSafeMap dataMap = new ZNJFSafeMap();
		
		try {
			//获取滚动页
			Object bannerObj = redisCached.getHashCached("tzb_app_index", "app_ad_lbt");
			log.debug("redis中的banner对象：" + JSON.parseArray(bannerObj.toString(), Advertis.class));
			List<Advertis> banners = JSON.parseArray(bannerObj.toString(), Advertis.class);
			dataMap.put("floatBanner", CollectionUtils.isNotEmpty(banners) ? banners : null);
			
			//获取平台最新公告
			ArrayList<Article> messageList = new ArrayList<Article>();
			Navigation navigation = new Navigation();
			navigation.setCode("lm0301");
			List<Navigation> navigations = navigationService.selectByEntityWhere(navigation);
			if (CollectionUtils.isNotEmpty(navigations)) {
				navigation = navigations.get(0);
				Page<Article> page = new Page<Article>(1, 1);
				HashMap<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("navigationId", navigation.getId());
				List<Article> articles = articleService.selectPlatformLastArticle(page, paramMap);
				if (CollectionUtils.isNotEmpty(articles)) {
					for (Article art : articles) {
						ZNJFSafeMap tempMap = new ZNJFSafeMap();
						tempMap.put("sid", art.getId());
						tempMap.put("title", art.getTitle());
						tempMap.put("newsAbstract", art.getNewsAbstract());
						tempMap.put("publishTime", null == art.getPublishDate() ? 0 : art.getPublishDate().getTime());
						tempMap.put("author", art.getAuthor());
						messageList.add(art);
					}
				}
			}
			dataMap.put("messageList", CollectionUtils.isNotEmpty(messageList) ? messageList : null);
			
			//获取首页tab
			BussinessConfig bussinessConfig = new BussinessConfig();
			bussinessConfig.setBussinessType(1);
			bussinessConfig.setBussinessIsopen(1);
			List<BussinessConfig> bussinessConfigs = bussinessConfigService.selectByEntityWhere(bussinessConfig);
			dataMap.put("configList", CollectionUtils.isNotEmpty(bussinessConfigs) ? bussinessConfigs : null);
			
			//获取平台基本数据
			//总用户
			Integer members = baseInfoService.selectTotalCountOfMembers();
			dataMap.put("totalInvestors",members);
			//总交易额
			BigDecimal totalTrade = projectBasicInfoService.selectTotalTrade();
			dataMap.put("totalTrade", null != totalTrade ? totalTrade.doubleValue() : 0);
			
			//获取推荐项目
			List<ProjectBasicInfo> recommendList = projectBasicInfoService.selectRecommendProjects(new Page<ProjectBasicInfo>(1, 3));
			dataMap.put("recommendList", CollectionUtils.isNotEmpty(recommendList) ? recommendList : null);
			
			//获取注册红包金额
			String couponAmount = GrpParaUtil.getValue("P30028", "reg_couponAmount");
			dataMap.put("couponAmount", Double.parseDouble(couponAmount));
			
			//获取新手预期年化
			String newUserRate = GrpParaUtil.getValue("P30028", "reg_newUserRate");
			dataMap.put("newUserRate", Double.parseDouble(newUserRate));
			
		} catch (Exception e) {
			log.error("homePage接口异常：" + e);
			return ZNJFUtils.buildResult("homePage", resMap, ZNJFResponseResult.ResultEnum.SERVICE_ERROR.getCode(), 
					ZNJFResponseResult.ResultEnum.SERVICE_ERROR.getDesc());
		}
		
		return ZNJFUtils.buildResult("homePage", resMap, dataMap, ZNJFResponseResult.ResultEnum.SUCCESS.getCode(), 
				ZNJFResponseResult.ResultEnum.SUCCESS.getDesc());
	}
}
