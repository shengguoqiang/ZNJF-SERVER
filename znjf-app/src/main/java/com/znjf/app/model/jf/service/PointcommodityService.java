package com.znjf.app.model.jf.service;

import com.tziba.model.jf.Pointcommodity;
import java.util.List;
import com.github.pagehelper.Page;
import com.tziba.generic.GenericService;

public interface PointcommodityService extends GenericService<Pointcommodity, String> {
 
	/**
	 * 查询积分商城推荐商品
	 * @param page 分页
	 * */
	List<Pointcommodity> selectRecommendCommodity(Page<Pointcommodity> page);
}