package com.znjf.app.model.jf.dao;

import java.util.List;

import com.tziba.generic.GenericDao;
import com.tziba.model.jf.Pointcommodity;

/** 积分商品表(JF_POINTCOMMODITY) **/
public interface PointcommodityDao extends GenericDao<Pointcommodity, String> {

	/**
	 * 查询积分商城推荐商品
	 * */
	List<Pointcommodity> selectRecommendCommodity();
	
}