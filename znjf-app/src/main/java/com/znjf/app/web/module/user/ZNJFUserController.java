package com.znjf.app.web.module.user;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tziba.common.ResponseVo;
import com.tziba.concurrent.ExecutorManager;
import com.tziba.hessian.jf.WsIntegralAccountService;
import com.tziba.hessian.mgc.MsgSenderService;
import com.tziba.hessian.vo.jf.IntegralVO;
import com.tziba.hessian.vo.mgc.SenderIntegrationVo;
import com.tziba.model.app.DeviceType;
import com.tziba.model.app.LoginRecord;
import com.tziba.model.hy.BaseInfo;
import com.tziba.model.hy.OperatorRole;
import com.tziba.model.sys.ParameterGroupDetail;
import com.tziba.servicediscovery.support.HessianServiceInvokeBuilder;
import com.tziba.tdp.domain.UserInfoDomain;
import com.tziba.tdp.sys.service.TdpUserService;
import com.tziba.tdp.utils.GrpParaUtil;
import com.tziba.tdp.utils.RedisCacheInfoUtil;
import com.tziba.tdp.utils.SysParaUtil;
import com.tziba.utils.DateUtils;
import com.tziba.utils.IdFactory;
import com.tziba.utils.MD5;
import com.znjf.app.model.app.service.DeviceTypeService;
import com.znjf.app.model.app.service.LoginRecordService;
import com.znjf.app.model.hy.service.BaseInfoService;
import com.znjf.app.model.hy.service.OperatorRoleService;
import com.znjf.app.web.common.base.ZNJFBaseController;
import com.znjf.app.web.common.base.ZNJFResponseResult;
import com.znjf.app.web.common.utils.ZNJFSafeMap;
import com.znjf.app.web.common.utils.ZNJFUtils;

/**
 * 中南金服App服务端-用户注册登录相关
 * 
 * @author FTD
 */
@Controller
@RequestMapping("/service")
public class ZNJFUserController extends ZNJFBaseController {

	@Autowired
	private BaseInfoService baseInfoService;

	@Autowired
	private TdpUserService tdpUserService;

	@Autowired
	private DeviceTypeService deviceTypeService;

	@Autowired
	private LoginRecordService loginRecordService;

	@Autowired
	private OperatorRoleService operatorRoleService;

	/**
	 * 判断用户是否已在平台注册
	 */
	@RequestMapping("/isTzbUser")
	@ResponseBody
	public Object isZnjfUser(HttpServletRequest request, HttpServletResponse response) {
		ZNJFSafeMap resMap = new ZNJFSafeMap();
		ZNJFSafeMap dataMap = new ZNJFSafeMap();

		// 获取参数
		String mobile = request.getParameter("mobile");
		log.debug("isTzbUser接口参数：{mobile: " + mobile + "}");

		// 校验参数是否为空
		if (StringUtils.isBlank(mobile)) {
			return ZNJFUtils.buildResult("isTzbUser", resMap, ZNJFResponseResult.ResultEnum.PARAM_INCOMPLETE.getCode(),
					ZNJFResponseResult.ResultEnum.PARAM_INCOMPLETE.getDesc());
		}

		// 校验参数有效性
		if (!ZNJFUtils.isMobile(mobile)) {
			return ZNJFUtils.buildResult("isTzbUser", resMap, ZNJFResponseResult.ResultEnum.NOT_MOBILE.getCode(),
					ZNJFResponseResult.ResultEnum.NOT_MOBILE.getDesc());
		}

		try {
			// 查询用户表
			BaseInfo baseInfo = new BaseInfo();
			baseInfo.setSmobile(mobile);
			List<BaseInfo> infos = baseInfoService.selectByEntityWhere(baseInfo);
			if (CollectionUtils.isNotEmpty(infos)) {
				dataMap.put("isExist", 1);// 用户已在平台注册
			} else {
				dataMap.put("isExist", 0);// 用户未在平台注册
			}
			return ZNJFUtils.buildResult("isTzbUser", resMap, dataMap, ZNJFResponseResult.ResultEnum.SUCCESS.getCode(),
					ZNJFResponseResult.ResultEnum.SUCCESS.getDesc());
		} catch (Exception e) {
			log.error("isTzbUser接口异常：" + e);
			return ZNJFUtils.buildResult("isTzbUser", resMap, ZNJFResponseResult.ResultEnum.SERVICE_ERROR.getCode(),
					ZNJFResponseResult.ResultEnum.SERVICE_ERROR.getDesc());
		}
	}

	/**
	 * 用户注册
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/regedit")
	@ResponseBody
	public Object register(HttpServletRequest request, HttpServletResponse response) {
		ZNJFSafeMap resMap = new ZNJFSafeMap();
		ZNJFSafeMap dataMap = new ZNJFSafeMap();

		// 获取参数
		// 公参
		final String platform = request.getParameter("platform");// 平台 1iOS 2Android
		final String deviceId = "1".equals(platform) ? request.getParameter("idfa") : request.getParameter("UUID");// 设置唯一标识
		final String deviceName = request.getParameter("deviceName");// 设备名称
		final String operateSys = request.getParameter("phoneVersion");// 操作系统
		String channel = request.getParameter("channel");// 来源渠道

		// 用户输入参数
		final String mobile = request.getParameter("mobile");
		String password = request.getParameter("password");
		String verCode = request.getParameter("verCode");
		String recommendMobile = request.getParameter("recommendMobile");
		log.debug("regedit接口参数：{mobile: " + mobile + ", password: ****, verCode: " + verCode + ", recommendMobile: "
				+ recommendMobile + "}");

		// 校验参数是否为空
		if (StringUtils.isBlank(mobile) || StringUtils.isBlank(password) || StringUtils.isBlank(verCode)) {
			return ZNJFUtils.buildResult("regedit", resMap, ZNJFResponseResult.ResultEnum.PARAM_INCOMPLETE.getCode(),
					ZNJFResponseResult.ResultEnum.PARAM_INCOMPLETE.getDesc());
		}

		// 校验参数有效性
		// 校验手机号
		if (!ZNJFUtils.isMobile(mobile)
				|| (StringUtils.isNotBlank(recommendMobile) && !ZNJFUtils.isMobile(recommendMobile))) {
			return ZNJFUtils.buildResult("regedit", resMap, ZNJFResponseResult.ResultEnum.NOT_MOBILE.getCode(),
					ZNJFResponseResult.ResultEnum.NOT_MOBILE.getDesc());
		}

		// 校验密码
		if (!ZNJFUtils.validatePassword(password)) {
			return ZNJFUtils.buildResult("regedit", resMap, ZNJFResponseResult.ResultEnum.ILLEGAL_PASSWORD.getCode(),
					ZNJFResponseResult.ResultEnum.ILLEGAL_PASSWORD.getDesc());
		}

		try {
			// 校验验证码
			String cachedVerCode = "";
			Object verCodeObj = redisCached.getCached(ZNJFUtils.getVerCodeValidPeriodKey("1", mobile));
			if (null != verCodeObj) {
				cachedVerCode = verCodeObj.toString();
			}
			if (null == verCodeObj || !verCode.equals(cachedVerCode)) {
				return ZNJFUtils.buildResult("regedit", resMap, ZNJFResponseResult.ResultEnum.VERCODE_ERROR.getCode(),
						ZNJFResponseResult.ResultEnum.VERCODE_ERROR.getDesc());
			}

			// 校验当前用户是否已在平台注册
			final BaseInfo curBaseInfo = new BaseInfo();
			curBaseInfo.setSmobile(mobile);
			List<BaseInfo> curBaseInfos = baseInfoService.selectByEntityWhere(curBaseInfo);
			if (CollectionUtils.isNotEmpty(curBaseInfos)) {
				return ZNJFUtils.buildResult("regedit", resMap, ZNJFResponseResult.ResultEnum.USER_EXIST.getCode(),
						ZNJFResponseResult.ResultEnum.USER_EXIST.getDesc());
			}

			// 校验推荐人是否是平台用户
			BaseInfo recBaseInfo = new BaseInfo();
			if (StringUtils.isNoneBlank(recommendMobile)) {
				recBaseInfo.setSmobile(recommendMobile);
				List<BaseInfo> recBaseInfos = baseInfoService.selectByEntityWhere(recBaseInfo);
				if (CollectionUtils.isEmpty(recBaseInfos)) {
					return ZNJFUtils.buildResult("regedit", resMap,
							ZNJFResponseResult.ResultEnum.RED_NOT_EXIST.getCode(),
							ZNJFResponseResult.ResultEnum.RED_NOT_EXIST.getDesc());
				}
				recBaseInfo = recBaseInfos.get(0);
			}

			// 注册信息入库
			curBaseInfo.setSloginpassword(MD5.encode(password));// 密码MD5加密
			curBaseInfo.setImembertype(1);// 用户类型：0本公司 1投资会员 2企业
			curBaseInfo.setIstatus(0);// 用户状态：0正常 1停用 2冻结
			curBaseInfo.setDregisterdate(DateUtils.getCurrentDateTime());// 用户注册时间
			curBaseInfo.setIisInvestment(0);// 用户是否有过投资：0没有 1有
			curBaseInfo.setIgrade(1);// 用户等级
			curBaseInfo.setDelFlag(0);// 用户是否删除 0未删除 1已删除
			curBaseInfo.setIsCertification(0);// 用户是否认证：0否 1只开通中金 2只开通存管 3双开
			// 客户端注册来源
			curBaseInfo.setIsourceClientType("1".equals(platform) ? 20 : 30);
			// 用户注册来源
			if (StringUtils.isNoneBlank(recBaseInfo.getSuserCode())) {
				curBaseInfo.setSsourcetype(30);
				curBaseInfo.setSsourcetypename("用户推荐");
				curBaseInfo.setSsourceCode(recBaseInfo.getSuserCode());
			} else if (StringUtils.isNoneBlank(channel)) {
				Set<ParameterGroupDetail> paramGroupDetails = GrpParaUtil.get("P10002");
				for (ParameterGroupDetail parameterGroupDetail : paramGroupDetails) {
					if (channel.contains(parameterGroupDetail.getSremark())) {
						curBaseInfo.setSsourcetype(Integer.parseInt(parameterGroupDetail.getSvalue()));
						curBaseInfo.setSsourcetypename(parameterGroupDetail.getSname());
						curBaseInfo.setSsourceCode(channel);
					} else {
						curBaseInfo.setSsourceCode(channel);
					}
				}
			}

			// 调用hessian 获取用户的username userCode
			String userName = "";
			String userCode = "";
			UserInfoDomain userInfo = tdpUserService.getUserInfo();
			if (null != userInfo) {
				userName = userInfo.getUserName();
				userCode = userInfo.getUserCode();
			}
			curBaseInfo.setSusername(userName);
			curBaseInfo.setSuserCode(userCode);

			// 用户信息入用户表
			baseInfoService.insert(curBaseInfo);

			// 开通积分账户
			IntegralVO integralVO = new IntegralVO();
			integralVO.setSuserId(curBaseInfo.getId());
			integralVO.setSuserCode(userCode);
			integralVO.setSuserName(userName);
			ResponseVo openAccount = HessianServiceInvokeBuilder.newBuilder().invoker(WsIntegralAccountService.class)
					.openAccount(integralVO);
			if (!openAccount.isSuccess()) {
				log.error("开通积分账户异常：" + openAccount.getMsg());
				return ZNJFUtils.buildResult("", resMap, openAccount.getErrorCode(), "积分账户异常：" + openAccount.getMsg());
			}

			// 生成userToken,并将其存入redis,且设置有效时间
			String userToken = IdFactory.getUUIDSerialNumber();
			Integer clientTokenTimeout = 5; // token有效时间(单位:小时)，默认为5
			String clientTokenTimeoutStr = SysParaUtil.get("clientTokenTimeout");
			if (StringUtils.isNotBlank(clientTokenTimeoutStr)) {
				clientTokenTimeout = Integer.parseInt(clientTokenTimeoutStr);
			}
			redisCached.addCachedExpireSec(userToken, curBaseInfo.getId(), clientTokenTimeout * 3600); // token为key
																										// uid为value
			redisCached.addCachedExpireSec(curBaseInfo.getId(), userToken, clientTokenTimeout * 3600); // uid为key
																										// token为value

			// 异步:发短信、记录设备、记录登录信息、将用户与角色绑定
			ExecutorManager.getInstance().execute(new Runnable() {

				public void run() {

					// 发短信
					SenderIntegrationVo sendVo = new SenderIntegrationVo();
					sendVo.setTelNo(mobile);
					sendVo.setTemplateMainCode("T013");
					try {
						HessianServiceInvokeBuilder.newBuilder().invoker(MsgSenderService.class)
								.sendIntegration(sendVo);
					} catch (Exception e) {
						log.error("发送短信异常:" + e);
					}

					// 记录设备
					DeviceType device = new DeviceType();
					device.setSdeviceId(deviceId);
					device.setSdeviceName(deviceName);
					List<DeviceType> existedDevices = deviceTypeService.selectByEntityWhere(device);
					DeviceType existedDevice = null;
					if (CollectionUtils.isNotEmpty(existedDevices)) {
						existedDevice = existedDevices.get(0);
						// 删除设备之前记录的相关信息
						deviceTypeService.deleteByPrimaryKey(existedDevice.getId());
					}
					device.setSoperationSys(operateSys);
					device.setSuserCode(curBaseInfo.getSuserCode());
					device.setSuserName(curBaseInfo.getSusername());
					device.setSuserId(curBaseInfo.getId());
					device.setSuserRealname(curBaseInfo.getSname());
					device.setItype(Integer.parseInt(platform));
					device.setIisPush(1);
					device.setTaddTime(new Date());
					device.setTfirstLogintime(new Date());
					device.setTlastLogintime(new Date());
					if (null != existedDevice) {
						// 保留设备之前记录中的添加时间、首次登录时间以及推送状态
						device.setTaddTime(existedDevice.getTaddTime());
						device.setTfirstLogintime(existedDevice.getTfirstLogintime());
						device.setIisPush(existedDevice.getIisPush());
					}
					deviceTypeService.insert(device);

					// 记录登录信息
					LoginRecord loginRecord = new LoginRecord();
					loginRecord.setTloginTime(new Date());
					loginRecord.setIendType(Integer.parseInt(platform));
					loginRecord.setSdeviceName(deviceName);
					loginRecord.setSuserId(curBaseInfo.getId());
					loginRecord.setSoperationSys(operateSys);
					loginRecord.setSdeviceid(deviceId);
					loginRecord.setSuserCode(curBaseInfo.getSuserCode());
					loginRecord.setSuserName(curBaseInfo.getSusername());
					loginRecord.setSuserRealname(curBaseInfo.getSname());
					loginRecordService.insert(loginRecord);

					// 将用户与角色绑定
					OperatorRole op = new OperatorRole();
					op.setSoperatorId(curBaseInfo.getId());
					op.setSroleId("0003");
					operatorRoleService.insert(op);
				}

			});

			// TODO:营销活动发放！！！

			// 拼接客户端数据
			dataMap.put("bid", curBaseInfo.getId());
			dataMap.put("suserCode", curBaseInfo.getSuserCode());
			dataMap.put("susername", curBaseInfo.getSusername());
			dataMap.put("sname", curBaseInfo.getSname());
			dataMap.put("smobile", curBaseInfo.getSmobile());
			dataMap.put("is_certification", curBaseInfo.getIsCertification());
			dataMap.put("userToken", userToken);

			return ZNJFUtils.buildResult("regedit", resMap, ZNJFResponseResult.ResultEnum.SUCCESS.getCode(),
					ZNJFResponseResult.ResultEnum.SUCCESS.getDesc());
		} catch (Exception e) {
			log.error("regedit接口异常：" + e);
			return ZNJFUtils.buildResult("regedit", resMap, ZNJFResponseResult.ResultEnum.SERVICE_ERROR.getCode(),
					ZNJFResponseResult.ResultEnum.SERVICE_ERROR.getDesc());
		}
	}

	/**
	 * 用户登录
	 */
	@RequestMapping("/login")
	@ResponseBody
	public Object login(HttpServletRequest request, HttpServletResponse response) {
		ZNJFSafeMap resMap = new ZNJFSafeMap();
		ZNJFSafeMap dataMap = new ZNJFSafeMap();

		// 获取参数
		// 公参
		final String platform = request.getParameter("platform");// 平台 1iOS 2Android
		final String deviceId = "1".equals(platform) ? request.getParameter("idfa") : request.getParameter("UUID");// 设置唯一标识
		final String deviceName = request.getParameter("deviceName");// 设备名称
		final String operateSys = request.getParameter("phoneVersion");// 操作系统

		// 用户输入参数
		String mobile = request.getParameter("username");
		String password = request.getParameter("password");
		log.debug("login接口参数：{username: " + mobile + ", password: ****}");

		// 校验参数是否非空
		if (StringUtils.isBlank(mobile) || StringUtils.isBlank(password)) {
			return ZNJFUtils.buildResult("login", resMap, ZNJFResponseResult.ResultEnum.PARAM_INCOMPLETE.getCode(),
					ZNJFResponseResult.ResultEnum.PARAM_INCOMPLETE.getDesc());
		}

		// 校验参数有效性
		// 校验手机号
		if (!ZNJFUtils.isMobile(mobile)) {
			return ZNJFUtils.buildResult("login", resMap, ZNJFResponseResult.ResultEnum.NOT_MOBILE.getCode(),
					ZNJFResponseResult.ResultEnum.NOT_MOBILE.getDesc());
		}
		// 校验密码
		if (!ZNJFUtils.validatePassword(password)) {
			return ZNJFUtils.buildResult("login", resMap, ZNJFResponseResult.ResultEnum.ILLEGAL_PASSWORD.getCode(),
					ZNJFResponseResult.ResultEnum.ILLEGAL_PASSWORD.getDesc());
		}

		try {
			// 校验当前用户是否存在
			BaseInfo curBaseInfo = new BaseInfo();
			curBaseInfo.setSmobile(mobile);
			List<BaseInfo> curBaseInfos = baseInfoService.selectByEntityWhere(curBaseInfo);
			if (CollectionUtils.isEmpty(curBaseInfos)) {
				return ZNJFUtils.buildResult("login", resMap, ZNJFResponseResult.ResultEnum.USER_NOTEXIST.getCode(),
						ZNJFResponseResult.ResultEnum.USER_NOTEXIST.getDesc());
			}

			// 校验当前登录是否大于最大登录尝试次数
			Integer loginTryTimes = 5;
			Integer maxLoginTryTimes = 5;
			// redis中获取登录尝试次数
			String loginTryTimesKey = RedisCacheInfoUtil.TZB_LOGIN_ERROR + "app_" + mobile + "_"
					+ DateUtils.dateToString(new Date(), "yyyyMMdd");
			Object loginTryTimesObj = redisCached.getCached(loginTryTimesKey);
			if (null != loginTryTimesObj) {
				loginTryTimes = (Integer) loginTryTimesObj + 1;
			}
			// 数据库中获取运行登录尝试次数
			String loginTryTimesStr = SysParaUtil.get("loginErrorTimes");
			if (StringUtils.isNotBlank(loginTryTimesStr)) {
				maxLoginTryTimes = Integer.parseInt(loginTryTimesStr);
			}
			if (loginTryTimes > maxLoginTryTimes) {
				return ZNJFUtils.buildResult("login", resMap, ZNJFResponseResult.ResultEnum.PWD_ERROR_FIVE.getCode(),
						ZNJFResponseResult.ResultEnum.PWD_ERROR_FIVE.getDesc());
			}

			// 根据手机号和密码查询用户
			curBaseInfo.setSloginpassword(MD5.encode(password));
			List<BaseInfo> existedUsers = baseInfoService.selectByEntityWhere(curBaseInfo);
			if (CollectionUtils.isEmpty(existedUsers)) {// 用户不存在
				// 登录尝试次数+1
				Integer loginTryTimeOut = 24;
				String loginTryTimeOutStr = SysParaUtil.get("password_error_times");
				if (StringUtils.isNotBlank(loginTryTimeOutStr)) {
					loginTryTimeOut = Integer.parseInt(loginTryTimeOutStr);
				}
				redisCached.updateCached(loginTryTimesKey, loginTryTimes, loginTryTimeOut * 3600);
				return ZNJFUtils.buildResult("login", resMap, ZNJFResponseResult.ResultEnum.USER_NOTCORRRECT.getCode(),
						ZNJFResponseResult.ResultEnum.USER_NOTCORRRECT.getDesc());
			}
			final BaseInfo existedUser = existedUsers.get(0);
			// 校验用户是否已删除
			if (existedUser.getDelFlag() != 0) {
				log.error("用户" + existedUser.getSusername() + "已被删除");
				return ZNJFUtils.buildResult("login", resMap, ZNJFResponseResult.ResultEnum.USER_STATUS_FAIL.getCode(),
						ZNJFResponseResult.ResultEnum.USER_STATUS_FAIL.getDesc());
			}

			// 校验用户是否是企业
			if (existedUser.getImembertype() == 2) {
				log.error("用户" + existedUser.getSusername() + "是企业用户");
				return ZNJFUtils.buildResult("login", resMap, ZNJFResponseResult.ResultEnum.USER_IS_INDUSTRY.getCode(),
						ZNJFResponseResult.ResultEnum.USER_IS_INDUSTRY.getDesc());
			}

			// 校验用户状态是否异常
			if (existedUser.getIstatus() != 0) {
				log.error("用户" + existedUser.getSusername() + "状态不正常");
				return ZNJFUtils.buildResult("login", resMap, ZNJFResponseResult.ResultEnum.USER_FROZEN.getCode(),
						ZNJFResponseResult.ResultEnum.USER_FROZEN.getDesc());
			}

			// 处理redis存储
			// 删除redis中原有存储
			Object token_old = redisCached.getCached(existedUser.getId());
			if (null != token_old) {
				redisCached.deleteCached(token_old.toString());
			}
			// 更新redis中的存储
			String userToken = IdFactory.getUUIDSerialNumber();
			Integer clientTokenTimeout = 5; // token有效时间(单位:小时)，默认为5
			String clientTokenTimeoutStr = SysParaUtil.get("clientTokenTimeout");
			if (StringUtils.isNotBlank(clientTokenTimeoutStr)) {
				clientTokenTimeout = Integer.parseInt(clientTokenTimeoutStr);
			}
			//TODO:aop中每次请求过来，需重置token在redis中的过期时间
			redisCached.addCachedExpireSec(userToken, existedUser.getId(), clientTokenTimeout * 3600); // token为key
			redisCached.addCachedExpireSec(existedUser.getId(), userToken, clientTokenTimeout * 3600); // uid为key
																										// token为value

			// 清空登录尝试次数
			redisCached.deleteCached(loginTryTimesKey);

			// 异步：记录设备、记录登录信息
			ExecutorManager.getInstance().execute(new Runnable() {

				public void run() {

					// 记录设备
					DeviceType device = new DeviceType();
					device.setSdeviceId(deviceId);
					device.setSdeviceName(deviceName);
					List<DeviceType> existedDevices = deviceTypeService.selectByEntityWhere(device);
					DeviceType existedDevice = null;
					if (CollectionUtils.isNotEmpty(existedDevices)) {
						existedDevice = existedDevices.get(0);
						// 删除设备之前记录的相关信息
						deviceTypeService.deleteByPrimaryKey(existedDevice.getId());
					}
					device.setSoperationSys(operateSys);
					device.setSuserCode(existedUser.getSuserCode());
					device.setSuserName(existedUser.getSusername());
					device.setSuserId(existedUser.getId());
					device.setSuserRealname(existedUser.getSname());
					device.setItype(Integer.parseInt(platform));
					device.setIisPush(1);
					device.setTaddTime(new Date());
					device.setTfirstLogintime(new Date());
					device.setTlastLogintime(new Date());
					if (null != existedDevice) {
						// 保留设备之前记录中的添加时间、首次登录时间以及推送状态
						device.setTaddTime(existedDevice.getTaddTime());
						device.setTfirstLogintime(existedDevice.getTfirstLogintime());
						device.setIisPush(existedDevice.getIisPush());
					}
					deviceTypeService.insert(device);

					// 记录登录信息
					LoginRecord loginRecord = new LoginRecord();
					loginRecord.setSuserId(existedUser.getId());
					List<LoginRecord> loginRecords = loginRecordService.selectByEntityWhere(loginRecord);
					if (CollectionUtils.isEmpty(loginRecords)) {// 无登录信息记录，发放App首次登录奖励
						//TODO:app首次登录奖励发放！！！
					}
					loginRecord.setSuserCode(existedUser.getSuserCode());
					loginRecord.setSuserName(existedUser.getSusername());
					loginRecord.setSuserRealname(existedUser.getSname());
					loginRecord.setTloginTime(new Date());
					loginRecord.setIendType(Integer.parseInt(platform));
					loginRecord.setSdeviceName(deviceName);
					loginRecord.setSoperationSys(operateSys);
					loginRecord.setSdeviceid(deviceId);
					loginRecordService.insert(loginRecord);
				}
			});
			
			//TODO:登录奖励活动发放！！！
			
			//拼接客户端数据
	        dataMap.put("bid", existedUser.getId());
	        dataMap.put("suserCode", existedUser.getSuserCode());
	        dataMap.put("susername", existedUser.getSusername());
	        dataMap.put("sname", existedUser.getSname());
	        dataMap.put("smobile", existedUser.getSmobile());
	        dataMap.put("is_certification", existedUser.getIsCertification());
	        dataMap.put("userToken", userToken);
	        
	        return ZNJFUtils.buildResult("login", resMap, dataMap, ZNJFResponseResult.ResultEnum.SUCCESS.getCode(), 
	        		ZNJFResponseResult.ResultEnum.SUCCESS.getDesc());
		} catch (Exception e) {
			log.error("login接口异常：" + e);
			return ZNJFUtils.buildResult("login", resMap, ZNJFResponseResult.ResultEnum.SERVICE_ERROR.getCode(),
					ZNJFResponseResult.ResultEnum.SERVICE_ERROR.getDesc());
		}

	}

}
