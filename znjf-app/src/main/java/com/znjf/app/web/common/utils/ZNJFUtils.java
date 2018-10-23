package com.znjf.app.web.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tziba.tdp.utils.RedisCacheInfoUtil;
import com.tziba.utils.DateUtils;
import com.tziba.utils.JsonUtils;

/**
 * 中南金服App服务端-工具类
 * @author FTD
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
	
	/**
     * 手机号验证
     * @param mobile
     */
    public static boolean isMobile(String mobile) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3-9][0-9]{9}$");
        m = p.matcher(mobile);
        b = m.matches();
        return b;
    }

    /**
     * 验证密码是否合法 数字和字母 长度是6-20位
     * @param password
     */
    public static boolean validatePassword(String password) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");
        m = p.matcher(password);
        b = m.matches();
        return b;
    }

    /**
     * 验证邮箱合法
     * @param password
     */
    public static boolean validateEmail(String email) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"); 
        m = p.matcher(email);
        b = m.matches();
        return b;
    }
    
    /**
     * 根据手机号和验证码类型获取
     * 规定时间内下发验证码次数---在redis中的key
     * */
    public static String getVerCodeTimesDuringCertainTimeKey(String type, String mobile) {
        String key = "";
        if ("1".equals(type)) {// 注册
            key = RedisCacheInfoUtil.TZB_REGISTER_CODE;
        } else if ("2".equals(type)) {// 找回密码
            key = RedisCacheInfoUtil.TZB_FINDPWD_CODE;
        } else if ("3".equals(type)) {// 修改绑定手机号
            key = RedisCacheInfoUtil.TZB_CHANGE_MOBILE;
        } else if ("4".equals(type)) {// 联合登陆绑定
            key = RedisCacheInfoUtil.TZB_BAND_DYMIC_CODE_SEND;
        } else if ("5".equals(type)) {// 修改存管交易密码
        	key = RedisCacheInfoUtil.TZB_CHANGE_CG_PWD;
        } else if ("6".equals(type)) {// 确认投资
        	key = RedisCacheInfoUtil.TZB_CG_INVEST;
        }
        return key + "app_times_" + mobile + "_" + DateUtils.dateToString(new Date(), "yyyyMMdd");
    }
    
    /**
     * 根据手机号和验证码类型获取
     * 验证码有效期---在redis中的key
     * */
    public static String getVerCodeValidPeriodKey(String type, String mobile) {
        String key = "";
        if ("1".equals(type)) {// 注册
            key = RedisCacheInfoUtil.TZB_REGISTER_CODE;
        } else if ("2".equals(type)) {// 找回密码
            key = RedisCacheInfoUtil.TZB_FINDPWD_CODE;
        } else if ("3".equals(type)) {// 修改绑定手机号
            key = RedisCacheInfoUtil.TZB_CHANGE_MOBILE;
        } else if ("4".equals(type)) {// 联合登陆绑定
            key = RedisCacheInfoUtil.TZB_BAND_DYMIC_CODE_SEND;
        } else if ("5".equals(type)) {// 修改存管交易密码
        	key = RedisCacheInfoUtil.TZB_CHANGE_CG_PWD;
        } else if ("6".equals(type)) {// 确认投资
        	key = RedisCacheInfoUtil.TZB_CG_INVEST;
        }
        return key + "app_" + mobile + "_" + DateUtils.dateToString(new Date(), "yyyyMMdd");
    }
    
    /**
     * 设置cookie
     * @param response 响应
     * @param key      cookie键
     * @param value    cookie值
     * */
    public static void addCookie(HttpServletResponse response, String key, String value) {
    	Cookie cookie = new Cookie(key, value);
    	cookie.setPath("/");
    	response.addCookie(cookie);
    }
    
    /**
     * 读取cookie
     * */
    public static String readCookie(HttpServletRequest request, String key) {
    	Cookie[] cookies = request.getCookies();
    	if (null != cookies) {
    		for (Cookie cookie : cookies) {
    			if (cookie.getName().equals(key)) {
    				return cookie.getValue();
    			}
    		}
		}
    	return "";
    }
}
