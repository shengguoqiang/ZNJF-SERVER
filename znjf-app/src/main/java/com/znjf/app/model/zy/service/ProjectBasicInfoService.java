package com.znjf.app.model.zy.service;

import com.tziba.model.zy.ProjectBasicInfo;

import java.math.BigDecimal;
import java.util.List;

import com.github.pagehelper.Page;
import com.tziba.generic.GenericService;

public interface ProjectBasicInfoService extends GenericService<ProjectBasicInfo, String> {
 
	/**
	 * 查询平台交易总额
	 * */
	BigDecimal selectTotalTrade();
	
	/**
	 * 查询推荐项目
	 * @param page 分页
	 * */
	List<ProjectBasicInfo> selectRecommendProjects(Page<ProjectBasicInfo> page);
}