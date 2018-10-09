package com.znjf.app.web.common.base;

import java.util.Date;
import com.tziba.tdp.utils.GrpParaUtil;
import com.tziba.utils.DateUtils;

/**
 * 中南金服App服务端-常量
 * */
public class ZNJFConstants {
	
	/**************************活动参数[开始]**********************************/
	
    /** 当事人 **/
    // 登录
    public static final String ACTIVE_CODE_LOGIN = "AC0023";
    public static final int ACTIVE_TYPE_LOGIN = 130;
    
    //app首次登录时间
    public static final String APP_ACTIVE_CODE_LOGIN = "AC0033";
    public static final int APP_ACTIVE_TYPE_LOGIN = 200;
    
    // 注册
    public static final String ACTIVE_CODE_REGISTER = "AC0013";
    public static final int ACTIVE_TYPE_REGISTER = 20;
    
    // 绑定邮箱
    public static final String ACTIVE_CODE_BINDEMAIL = "AC0016";
    public static final int ACTIVE_TYPE_BINDEMAIL = 140;
    
    // 参与投资
    public static final String ACTIVE_CODE_INVEST = "AC0022";
    public static final int ACTIVE_TYPE_INVEST = 60;
    
    // 开通托管账户
    public static final String ACTIVE_CODE_OPEN = "AC0027";
    public static final int ACTIVE_TYPE_OPEN = 30;
    
    // 首次充值
    public static final String ACTIVE_CODE_RECHARGE = "AC0017";
    public static final int ACTIVE_TYPE_RECHARGE = 50;
    
    // 首次绑定银行卡
    public static final String ACTIVE_CODE_BINDBANK = "AC0018";
    public static final int ACTIVE_TYPE_BINDBANK = 40;
    
    
    /** 推广人 **/
    // 首投推荐
    public static final String ACTIVE_CODE_INVEST_RECOMMEND = "AC0024";
    public static final int ACTIVE_TYPE_INVEST_RECOMMEND = 160;
    
    // 开通托管账户推荐
    public static final String ACTIVE_CODE_RECOMMEND = "AC0015";
    public static final int ACTIVE_TYPE_RECOMMEND = 150;
    
    // 注册推荐
    public static final String ACTIVE_CODE_REGISTER_RECOMMEND = "AC0014";
    public static final int ACTIVE_TYPE_REGISTER_RECOMMEND = 70;
    
    /**************************活动参数[结束]**********************************/
    
    /*************************项目类型模板CODE[开始]****************************/
    
    /**App首页BANNER*/  
    public static final String HOME_PAGE_INDEX_AD_CODE = "R20015";
    
    /**wap新版积分商城首页BANNER*/
    public static final String JFCENTER_BANNER_AD_CODE = "R20016";
    
    /**wap新版积分商城商品运营位*/
    public static final String JFCENTER_PRODUCT_AD_CODE = "R20017";
    
    /*************************项目类型模板CODE[结束]****************************/
    
    /*************************redisKey[开始]**********************************/
    
	/**当前设备下发短信次数标识*/
	public static final String DEVICE_SEND_TIMES_KEY = "znjf_app_device_times_"
            + DateUtils.dateToString(new Date(), "yyyyMMdd");
	
	/**当前设备下发短信次数*/
	public static final Integer DEVICE_SEND_TIMES = 100;
	
	/*************************redisKey[结束]*********************************/
	
	/***************************CookieKey[开始]******************************/
	
	/**用户token*/
	public static final String COOKIE_TOKEN_KEY = "znjf_app_cookie_token_key";
	
	/**客户端类型*/
	public static final String COOKIE_CLIENTTYPE_KEY = "znjf_app_cookie_clientType_key";
	
	/***************************CookieKey[结束]******************************/
	
	/****************************其他常量************************************/
	
	/**
	 * 获取银行存管托管方id
	 */
	public static String trusteeId() {
		return GrpParaUtil.getValue("P10007", "C00001");
	}
	
	/**
	 * 获取存储在redis中图形验证码的key
	 * @param uid 用户id
	 * */
	public static String imageCodeKey(String uid) {
		return "znjf_app_redis_imageCode_"+uid+"_key";
	}
	
	/**异常信息名称*/
    public static final String ZNJF_EXCEPTION_NEED_LOGIN = "znjf_exception_need_login";
}
