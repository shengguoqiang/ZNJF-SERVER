package com.znjf.app.web.module.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tziba.model.hy.BaseInfo;
import com.znjf.app.model.hy.service.BaseInfoService;
import com.znjf.app.web.common.base.ZNJFBaseController;
import com.znjf.app.web.common.utils.ZNJFSafeMap;
import com.znjf.app.web.common.utils.ZNJFUtils;

/**
 * 中南金服App服务端-用户注册登录相关
 * @author wjdqs
 * */
@Controller
@RequestMapping("/service")
public class ZNJFUserController extends ZNJFBaseController {
	
	@Autowired
	private BaseInfoService baseInfoService;
	
	@RequestMapping("/isTzbUser")
	@ResponseBody
	public Object isZnjfUser(HttpServletRequest request, HttpServletResponse response) {
		ZNJFSafeMap resMap = new ZNJFSafeMap();
		String mobile = request.getParameter("mobile");
		log.debug("isTzbUser接口参数：{mobile: " + mobile + "}");
		
		BaseInfo baseInfo = new BaseInfo();
		baseInfo.setSmobile(mobile);
		List<BaseInfo> infos = baseInfoService.selectByEntityWhere(baseInfo);
		if (CollectionUtils.isNotEmpty(infos)) {
			return ZNJFUtils.buildResult("isTzbUser", resMap, -1, "用户已存在");
		}
		
		return ZNJFUtils.buildResult("isTzbUser", resMap, 200, "操作成功");
	}

}
