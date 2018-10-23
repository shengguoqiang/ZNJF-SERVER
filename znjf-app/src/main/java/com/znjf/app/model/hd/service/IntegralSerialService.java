package com.znjf.app.model.hd.service;

import com.tziba.model.hd.IntegralSerial;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.tziba.generic.GenericService;

public interface IntegralSerialService extends GenericService<IntegralSerial, String> {
 
	/**
	 * 根据积分类型查询积分流水列表
	 * */
	List<IntegralSerial> selectScoreListBySort(Page<IntegralSerial> page, Map<String, Object> paramMap);
}