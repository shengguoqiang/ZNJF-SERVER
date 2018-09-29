package com.znjf.app.model.hy.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.tziba.exception.ServiceException;
import com.tziba.generic.GenericDao;
import com.tziba.generic.GenericServiceImpl;

import com.znjf.app.model.hy.dao.BaseInfoDao;
import com.tziba.model.hy.BaseInfo;
import com.znjf.app.model.hy.service.BaseInfoService;

@Service
public class BaseInfoServiceImpl extends GenericServiceImpl<BaseInfo, String> implements
		BaseInfoService {

	@Autowired
	BaseInfoDao baseInfoDao;

	
	@Override
	public GenericDao<BaseInfo, String> getDao() {
		return baseInfoDao;
	}

	
	

}