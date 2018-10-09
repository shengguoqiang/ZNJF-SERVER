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

import com.znjf.app.model.app.dao.LoginRecordDao;
import com.tziba.model.app.LoginRecord;
import com.znjf.app.model.app.service.LoginRecordService;

@Service
public class LoginRecordServiceImpl extends GenericServiceImpl<LoginRecord, String> implements
		LoginRecordService {

	@Autowired
	LoginRecordDao loginRecordDao;

	
	@Override
	public GenericDao<LoginRecord, String> getDao() {
		return loginRecordDao;
	}

	
	

}