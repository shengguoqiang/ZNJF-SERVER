package com.znjf.app.model.zx.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.tziba.exception.ServiceException;
import com.tziba.generic.GenericDao;
import com.tziba.generic.GenericServiceImpl;

import com.znjf.app.model.zx.dao.NavigationDao;
import com.tziba.model.zx.Navigation;
import com.znjf.app.model.zx.service.NavigationService;

@Service
public class NavigationServiceImpl extends GenericServiceImpl<Navigation, String> implements
		NavigationService {

	@Autowired
	NavigationDao navigationDao;

	
	@Override
	public GenericDao<Navigation, String> getDao() {
		return navigationDao;
	}

	
	

}