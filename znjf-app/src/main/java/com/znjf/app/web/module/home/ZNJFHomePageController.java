package com.znjf.app.web.module.home;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

	/**
	 * 首页
	 * @author dqs
	 * */
	@RequestMapping("/homePage")
	@ResponseBody
	public Object homePage(HttpServletRequest request, HttpServletResponse response) {
		ZNJFSafeMap resMap = new ZNJFSafeMap();
		ZNJFSafeMap dataMap = new ZNJFSafeMap();
		return ZNJFUtils.buildResult("homePage", resMap, dataMap, ZNJFResponseResult.ResultEnum.SUCCESS.getCode(), 
				ZNJFResponseResult.ResultEnum.SUCCESS.getDesc());
	}
}
