package com.znjf.app.model.zx.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.tziba.exception.ServiceException;
import com.tziba.generic.GenericDao;
import com.tziba.generic.GenericServiceImpl;

import com.znjf.app.model.zx.dao.ArticleDao;
import com.tziba.model.zx.Article;
import com.znjf.app.model.zx.service.ArticleService;

@Service
public class ArticleServiceImpl extends GenericServiceImpl<Article, String> implements
		ArticleService {

	@Autowired
	ArticleDao articleDao;

	
	@Override
	public GenericDao<Article, String> getDao() {
		return articleDao;
	}


	@Override
	public List<Article> selectPlatformLastArticle(Page<Article> page, Map<String, Object> paramMap) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		return articleDao.selectPlatformLastArticle(paramMap);
	}

	
	

}