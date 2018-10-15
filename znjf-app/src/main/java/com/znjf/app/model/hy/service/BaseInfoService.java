package com.znjf.app.model.hy.service;

import com.tziba.model.hy.BaseInfo;
import com.tziba.generic.GenericService;

public interface BaseInfoService extends GenericService<BaseInfo, String> {
 
	/**
	 * 查询平台总用户量
	 * */
	Integer selectTotalCountOfMembers();
}