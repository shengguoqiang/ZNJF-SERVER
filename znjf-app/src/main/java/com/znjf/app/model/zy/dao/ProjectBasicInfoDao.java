package com.znjf.app.model.zy.dao;

import java.math.BigDecimal;
import java.util.List;
import com.tziba.generic.GenericDao;
import com.tziba.model.zy.ProjectBasicInfo;

/** 项目基础信息表(ZY_PROJECT_BASIC_INFO) **/
public interface ProjectBasicInfoDao extends GenericDao<ProjectBasicInfo, String> {
	
	/**
	 * 查询平台交易总额
	 * */
	BigDecimal selectTotalTrade();
	
	/**
	 * 查询推荐项目
	 * */
	List<ProjectBasicInfo> selectRecommendProjects();
}