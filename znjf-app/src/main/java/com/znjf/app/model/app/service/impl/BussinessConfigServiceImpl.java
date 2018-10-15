package com.znjf.app.model.app.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.tziba.exception.ServiceException;
import com.tziba.generic.GenericDao;
import com.tziba.generic.GenericServiceImpl;

import com.znjf.app.model.app.dao.BussinessConfigDao;
import com.tziba.model.app.BussinessConfig;
import com.znjf.app.model.app.service.BussinessConfigService;

@Service
public class BussinessConfigServiceImpl extends GenericServiceImpl<BussinessConfig, String> implements
		BussinessConfigService {

	@Autowired
	BussinessConfigDao bussinessConfigDao;

	
	@Override
	public GenericDao<BussinessConfig, String> getDao() {
		return bussinessConfigDao;
	}

	
	

}