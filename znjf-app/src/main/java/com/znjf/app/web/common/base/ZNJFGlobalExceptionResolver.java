package com.znjf.app.web.common.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 中南金服App服务端-全局异常处理
 * */
public class ZNJFGlobalExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		ex.printStackTrace();
		String exMessage = ex.getMessage();
		if (exMessage.equals(ZNJFConstants.ZNJF_EXCEPTION_NEED_LOGIN)) {
			return new ModelAndView("error/redirect");
		}
		return new ModelAndView("error/500");
	}

}
