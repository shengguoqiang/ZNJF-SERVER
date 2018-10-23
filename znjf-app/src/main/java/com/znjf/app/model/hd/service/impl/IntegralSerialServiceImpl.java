package com.znjf.app.model.hd.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.tziba.exception.ServiceException;
import com.tziba.generic.GenericDao;
import com.tziba.generic.GenericServiceImpl;

import com.znjf.app.model.hd.dao.IntegralSerialDao;
import com.tziba.model.hd.IntegralSerial;
import com.znjf.app.model.hd.service.IntegralSerialService;

@Service
public class IntegralSerialServiceImpl extends GenericServiceImpl<IntegralSerial, String> implements
		IntegralSerialService {

	@Autowired
	IntegralSerialDao integralSerialDao;

	
	@Override
	public GenericDao<IntegralSerial, String> getDao() {
		return integralSerialDao;
	}


	@Override
	public List<IntegralSerial> selectScoreListBySort(Page<IntegralSerial> page, Map<String, Object> paramMap) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		return integralSerialDao.selectScoreListBySort(paramMap);
	}

	
	

}