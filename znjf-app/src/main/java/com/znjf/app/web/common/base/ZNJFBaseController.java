package com.znjf.app.web.common.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.tziba.cache.redis.ICached;

/**
 * 中南金服App服务端-控制器基类，后续创建的控制器均要继承此类
 * @author FTD
 * */
public class ZNJFBaseController {

    /** 日志 */
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	/** redis缓存 */
	@Autowired
	protected ICached redisCached;

}
