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

import com.znjf.app.model.hy.dao.OperatorRoleDao;
import com.tziba.model.hy.OperatorRole;
import com.znjf.app.model.hy.service.OperatorRoleService;

@Service
public class OperatorRoleServiceImpl extends GenericServiceImpl<OperatorRole, String> implements
		OperatorRoleService {

	@Autowired
	OperatorRoleDao operatorRoleDao;

	
	@Override
	public GenericDao<OperatorRole, String> getDao() {
		return operatorRoleDao;
	}

	
	

}