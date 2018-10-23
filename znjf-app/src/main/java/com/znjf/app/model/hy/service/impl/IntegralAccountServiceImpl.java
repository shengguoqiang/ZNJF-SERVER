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

import com.znjf.app.model.hy.dao.IntegralAccountDao;
import com.tziba.model.hy.IntegralAccount;
import com.znjf.app.model.hy.service.IntegralAccountService;

@Service
public class IntegralAccountServiceImpl extends GenericServiceImpl<IntegralAccount, String> implements
		IntegralAccountService {

	@Autowired
	IntegralAccountDao integralAccountDao;

	
	@Override
	public GenericDao<IntegralAccount, String> getDao() {
		return integralAccountDao;
	}

	
	

}