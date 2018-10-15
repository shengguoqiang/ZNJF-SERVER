package com.znjf.app.model.zx.service;

import com.tziba.model.zx.Article;

import java.util.List;
import java.util.Map;
import com.github.pagehelper.Page;
import com.tziba.generic.GenericService;

public interface ArticleService extends GenericService<Article, String> {
 
	/**
	 * 首页-查询平台最近公告
	 * */
	List<Article> selectPlatformLastArticle(Page<Article> page, Map<String, Object> paramMap); 
}