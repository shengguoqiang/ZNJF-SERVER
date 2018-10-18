package com.znjf.app.model.jf.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tziba.generic.GenericDao;
import com.tziba.generic.GenericServiceImpl;
import com.tziba.model.jf.Pointcommodity;
import com.znjf.app.model.jf.dao.PointcommodityDao;
import com.znjf.app.model.jf.service.PointcommodityService;

@Service
public class PointcommodityServiceImpl extends GenericServiceImpl<Pointcommodity, String> implements
		PointcommodityService {

	@Autowired
	PointcommodityDao pointcommodityDao;

	
	@Override
	public GenericDao<Pointcommodity, String> getDao() {
		return pointcommodityDao;
	}


	@Override
	public List<Pointcommodity> selectRecommendCommodity(Page<Pointcommodity> page) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		return pointcommodityDao.selectRecommendCommodity();
	}

	
	

}