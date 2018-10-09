package com.znjf.app.web.common.utils;

import java.util.HashMap;

/**
 * 中南金服App服务端-安全字典
 * @author FTD
 * */
public class ZNJFSafeMap extends HashMap<String,Object> {

	private static final long serialVersionUID = 1L;
	
	@Override
	public Object put(String key, Object value) {
        if (null == value) {
			value = "";
		}
		return super.put(key, value);
	}

}
