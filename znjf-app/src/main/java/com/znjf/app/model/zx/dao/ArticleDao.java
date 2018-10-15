package com.znjf.app.model.zx.dao;

import java.util.Map;

import com.github.pagehelper.Page;
import com.tziba.generic.GenericDao;
import com.tziba.model.zx.Article;

/** 文章表(ZX_ARTICLE) **/
public interface ArticleDao extends GenericDao<Article, String> {

	/**
	 * 首页-查询平台最近公告
	 * */
	Page<Article> selectPlatformLastArticle(Map<String, Object> paramMap);
}