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

import com.znjf.app.model.app.dao.DeviceTypeDao;
import com.tziba.model.app.DeviceType;
import com.znjf.app.model.app.service.DeviceTypeService;

@Service
public class DeviceTypeServiceImpl extends GenericServiceImpl<DeviceType, String> implements
		DeviceTypeService {

	@Autowired
	DeviceTypeDao deviceTypeDao;

	
	@Override
	public GenericDao<DeviceType, String> getDao() {
		return deviceTypeDao;
	}

	
	

}