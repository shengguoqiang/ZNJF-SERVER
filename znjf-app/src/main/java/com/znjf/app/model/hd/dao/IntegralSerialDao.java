package com.znjf.app.model.hd.dao;

import java.util.List;
import java.util.Map;

import com.tziba.generic.GenericDao;
import com.tziba.model.hd.IntegralSerial;

/** 用户积分流水表(HD_INTEGRAL_SERIAL) **/
public interface IntegralSerialDao extends GenericDao<IntegralSerial, String> {
	
	/**
	 * 根据积分类型查询积分流水列表
	 * */
	List<IntegralSerial> selectScoreListBySort(Map<String, Object> paramMap);
}