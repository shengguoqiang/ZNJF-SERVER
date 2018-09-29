package com.znjf.app.web.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import com.tziba.utils.JsonUtils;

/**
 * 中南金服App服务端-工具类
 * @author wjdqs
 * */
public class ZNJFUtils {

	/**日志*/
	private static Logger log = LoggerFactory.getLogger(ZNJFUtils.class);

	/**
	 * 接口结果处理方法
	 * @param method  方法名
	 * @param resMap  结果map
	 * @param code    状态码
	 * @param message 状态描述
	 * */
	public static Object buildResult(String method, Map<String, Object>resMap, Integer code, String message) {
		resMap.put("code", code);
		resMap.put("message", message);
	    log.debug(method+"接口返回："+JsonUtils.toString(resMap));
	    return resMap;
	}
	
	/**
	 * 接口结果处理方法
	 * @param method  方法名
	 * @param resMap  结果map
	 * @param dataMap 数据map
	 * @param code    状态码
	 * @param message 状态描述
	 * */
	public static Object buildResult(String method, Map<String, Object>resMap, Map<String, Object>dataMap, Integer code, String message) {
		resMap.put("code", code);
		resMap.put("message", message);
		resMap.put("data", dataMap);
	    log.debug(method+"接口返回："+JsonUtils.toString(resMap));
	    return resMap;
	}
}
