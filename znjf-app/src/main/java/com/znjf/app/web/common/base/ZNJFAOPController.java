package com.znjf.app.web.common.base;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;

import com.alibaba.fastjson.JSON;
import com.tziba.tdp.utils.SysParaUtil;
import com.znjf.app.web.common.utils.ZNJFSafeMap;
import com.znjf.app.web.common.utils.ZNJFUtils;

/**
 * 中南金服App服务端-aop
 */
public class ZNJFAOPController extends ZNJFBaseController {

	/**
	 * 请求进入控制器方法前调用
	 */
	public void doBefore(JoinPoint jp) throws Exception {
		try {
			log.info("客户端请求进入拦截器");
			Object[] args = jp.getArgs();
			if (args.length == 2) {
				HttpServletRequest request = null;
				HttpServletResponse response = null;
				if (args[0] instanceof HttpServletRequest)
					request = (HttpServletRequest) args[0];
				if (args[1] instanceof HttpServletResponse)
					response = (HttpServletResponse) args[1];
				//获取方法名
				String uri = request.getRequestURI();
				String method = uri.split("/service/")[1];
				log.info("**正在请求【"+method+"】方法**");
				
				//Cookie中设置token
				String userToken = request.getParameter("userToken");//客户端请求url中获取
				if (StringUtils.isBlank(userToken)) {
					userToken = ZNJFUtils.readCookie(request, ZNJFConstants.COOKIE_TOKEN_KEY);//客户端请求cookie中获取
				}
				if (StringUtils.isNotBlank(userToken)) {
					ZNJFUtils.addCookie(response, ZNJFConstants.COOKIE_TOKEN_KEY, userToken);
				}
				
				//校验用户登录有效性
				if (StringUtils.isNotBlank(userToken)) {
					String uid = (String) redisCached.getCached(userToken);
					if (StringUtils.isBlank(uid)) {
						if (method.contains("h5_")) {//web页面
							throw new IllegalArgumentException(ZNJFConstants.ZNJF_EXCEPTION_NEED_LOGIN);
						} else {//原生页面
							ZNJFSafeMap resMap = new ZNJFSafeMap();
							Object result = ZNJFUtils.buildResult("aop", resMap, ZNJFResponseResult.ResultEnum.USER_NOTEXIST.getCode(), 
									ZNJFResponseResult.ResultEnum.USER_NOTEXIST.getDesc());
							PrintWriter writer = response.getWriter();
							writer.write(result.toString());
							writer.flush();
							writer.close();
						}
					}
					return;
				}
				
				//客户端更新token有效期
				if (StringUtils.isNotBlank(userToken) && 
						StringUtils.isNotBlank((String)redisCached.getCached(userToken))) {
					String clientTokenTimeout = SysParaUtil.get("clientTokenTimeout");
					Integer timeout = 5 * 3600;//默认有效期为5分钟
					if (StringUtils.isNotBlank(clientTokenTimeout)) {
						timeout = Integer.parseInt(clientTokenTimeout) * 3600;
					}
					String uid = (String) redisCached.getCached(userToken);
					redisCached.updateCached(userToken, uid, timeout);
					redisCached.updateCached(uid, userToken, timeout);;
				}
			}
			log.info("客户端请求离开拦截器");
		} catch (Exception e) {
			log.error("aop异常：" + e);
		}
	}
}
