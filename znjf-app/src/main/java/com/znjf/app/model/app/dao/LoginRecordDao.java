package com.znjf.app.model.app.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import com.tziba.generic.GenericDao;
import com.tziba.model.app.LoginRecord;

/** 用户登录设备记录表(APP_LOGIN_RECORD) **/
public interface LoginRecordDao extends GenericDao<LoginRecord, String> {


	/** codegen **/
}