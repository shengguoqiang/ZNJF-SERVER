package com.znjf.app.model.hy.dao;

import com.tziba.generic.GenericDao;
import com.tziba.model.hy.BaseInfo;

/** 登录用户表(HY_BASE_INFO) **/
public interface BaseInfoDao extends GenericDao<BaseInfo, String> {
	
	/**
	 * 查询平台总用户量
	 * */
	Integer selectTotalCountOfMembers();

}