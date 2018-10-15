package com.znjf.app.web.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tziba.common.ResponseVo;
import com.tziba.hessian.mgc.MsgSenderService;
import com.tziba.hessian.vo.mgc.SenderIntegrationVo;
import com.tziba.model.hy.BaseInfo;
import com.tziba.servicediscovery.support.HessianServiceInvokeBuilder;
import com.tziba.tdp.utils.SysParaUtil;
import com.znjf.app.model.hy.service.BaseInfoService;
import com.znjf.app.web.common.base.ZNJFBaseController;
import com.znjf.app.web.common.base.ZNJFConstants;
import com.znjf.app.web.common.base.ZNJFResponseResult;

/**
 * 中南金服App服务端-短信验证码
 * 
 * @author FTD
 */
@Controller
@RequestMapping("/service")
public class ZNJFVerCodeController extends ZNJFBaseController {
	
	@Autowired
	private BaseInfoService baseInfoService;

	@RequestMapping("/verfication")
	@ResponseBody
	public Object verCode(HttpServletRequest request, HttpServletResponse response) {
		ZNJFSafeMap resMap = new ZNJFSafeMap();

		// 获取请求参数
		String mobile = request.getParameter("mobile");
		String type = request.getParameter("type");// 1、注册 2、忘记密码 3、修改平台绑定手机号 4、联合登录 5、修改存管交易密码 6、确认投资
		log.debug("verfication接口参数：{mobile: " + mobile + ", type:" + type + "}");

		// 校验参数
		if (StringUtils.isBlank(mobile) || StringUtils.isBlank(type)) {
			return ZNJFUtils.buildResult("verfication", resMap,
					ZNJFResponseResult.ResultEnum.PARAM_INCOMPLETE.getCode(),
					ZNJFResponseResult.ResultEnum.PARAM_INCOMPLETE.getDesc());
		}

		// 校验参数有效性
		if (!ZNJFUtils.isMobile(mobile)) {
			return ZNJFUtils.buildResult("verfication", resMap, ZNJFResponseResult.ResultEnum.NOT_MOBILE.getCode(),
					ZNJFResponseResult.ResultEnum.NOT_MOBILE.getDesc());
		}

		try {
			// 校验当前用户的有效性
			// 1.注册或者修改平台绑定手机号--用户表中应该不存在当前用户；
			// 2.其他类型--用户表中应该存在当前用户
			BaseInfo baseInfo = new BaseInfo();
			baseInfo.setSmobile(mobile);
			List<BaseInfo> baseInfos = baseInfoService.selectByEntityWhere(baseInfo);
			if (("1".equals(type) || "3".equals(type)) && CollectionUtils.isNotEmpty(baseInfos)) {
				return ZNJFUtils.buildResult("verfication", resMap, ZNJFResponseResult.ResultEnum.USER_EXIST.getCode(), 
						ZNJFResponseResult.ResultEnum.USER_EXIST.getDesc());
			}
			if (!"1".equals(type) && !"3".equals(type) && CollectionUtils.isEmpty(baseInfos)) {
				return ZNJFUtils.buildResult("verfication", resMap, ZNJFResponseResult.ResultEnum.BASEINFO_NOT_EXIST.getCode(), 
						ZNJFResponseResult.ResultEnum.BASEINFO_NOT_EXIST.getDesc());
			}
			
			// 校验当前设备下发短信次数是否达到上限
			// 当前设备下发短信次数，默认为5
			Integer deviceTimes = 5;
			// 获取用户唯一标识
			String deviceId = request.getParameter("UUID");;
			Object deviceTimesObj = redisCached.getCached(ZNJFConstants.DEVICE_SEND_TIMES_KEY + deviceId);
			if (null != deviceTimesObj) {
				deviceTimes = (Integer) deviceTimesObj;
			}
			if (deviceTimes > ZNJFConstants.DEVICE_SEND_TIMES) {
				return ZNJFUtils.buildResult("verfication", resMap,
						ZNJFResponseResult.ResultEnum.DEVICE_SEND_TIMES.getCode(),
						ZNJFResponseResult.ResultEnum.DEVICE_SEND_TIMES.getDesc());
			}

			// 校验短信验证码下发是否频繁
			// 规定时间内短信验证码下发次数
			Integer dbTotalTimes = 0;
			Integer redisTotalTimes = 0;
			// 从数据库中获取次数
			String totalTimesStr = SysParaUtil.get("totalTime");
			if (StringUtils.isNotBlank(totalTimesStr)) {
				dbTotalTimes = Integer.parseInt(totalTimesStr);
			}
			// 从redis中获取次数
			Object totalTimesObj = redisCached.getCached(ZNJFUtils.getVerCodeTimesDuringCertainTimeKey(type, mobile));
			if (null != totalTimesObj) {
				redisTotalTimes = (Integer) totalTimesObj;
			}
			if (redisTotalTimes > dbTotalTimes) {
				return ZNJFUtils.buildResult("verfication", resMap,
						ZNJFResponseResult.ResultEnum.SEND_FREQUENTLY.getCode(),
						ZNJFResponseResult.ResultEnum.SEND_FREQUENTLY.getDesc());
			}

			// 组装短信模板，发送短信
			Integer verCode = new Random().nextInt(899999) + 100000;
			log.debug("Identify_Code:" + verCode);
			SenderIntegrationVo sendVo = new SenderIntegrationVo();
			sendVo.setTelNo(mobile);
			Map<String, Object> verParam = new HashMap<String, Object>();
			verParam.put("Identify_Code", String.valueOf(verCode));
			sendVo.setContentParam(verParam);
			String templateMainCode = "";
			if ("1".equals(type)) {// 注册
				templateMainCode = "T012";
			} else if ("2".equals(type)) {// 忘记密码
				templateMainCode = "T009";
			} else if ("3".equals(type)) {// 修改平台绑定手机号
				templateMainCode = "T015";
			} else if ("4".equals(type)) {// 联合登录
				templateMainCode = "XXMB0002";
			} else if ("5".equals(type)) {// 修改存管交易密码
				templateMainCode = "T015";
			} else if ("6".equals(type)) {// 确认投资
				templateMainCode = "T015";
			}
			sendVo.setTemplateMainCode(templateMainCode);
			@SuppressWarnings("rawtypes")
			ResponseVo responseVo = HessianServiceInvokeBuilder.newBuilder().invoker(MsgSenderService.class)
					.sendIntegration(sendVo);
			if (!responseVo.isSuccess()) {
				log.error("下发验证码异常：" + responseVo.getMsg());
				return ZNJFUtils.buildResult("verfication", resMap, 500, "验证码获取失败，请重新获取！");
			}

			// 短信发送成功
			// 设置短信有效时间(单位:分钟)
			Integer verCodeTimeOut = 10;
			String verCodeTimeOutStr = SysParaUtil.get("ValifyCodeTimeOut");
			if (StringUtils.isNotBlank(verCodeTimeOutStr)) {
				verCodeTimeOut = Integer.parseInt(verCodeTimeOutStr);
			}
			redisCached.updateCached(ZNJFUtils.getVerCodeValidPeriodKey(type, mobile), verCode, verCodeTimeOut * 60);
			// 规定时间内短信验证码下发次数+1
			// 规定时间(单位:小时)，默认24小时
			Integer sysSendTimes = 24;
			String sysSendTimesStr = SysParaUtil.get("sendCodeTimeOut");
			if (StringUtils.isNotBlank(sysSendTimesStr)) {
				sysSendTimes = Integer.parseInt(sysSendTimesStr);
			}
			redisCached.updateCached(ZNJFUtils.getVerCodeTimesDuringCertainTimeKey(type, mobile), ++redisTotalTimes,
					sysSendTimes * 3600);
			// 当前设备下发短信次数+1
			redisCached.updateCached(ZNJFConstants.DEVICE_SEND_TIMES_KEY + deviceId, ++deviceTimes, 24 * 3600);

			return ZNJFUtils.buildResult("verfication", resMap, ZNJFResponseResult.ResultEnum.SUCCESS.getCode(),
					ZNJFResponseResult.ResultEnum.SUCCESS.getDesc());
		} catch (Exception e) {
			log.error("verfication接口异常：" + e);
			return ZNJFUtils.buildResult("verfication", resMap, ZNJFResponseResult.ResultEnum.SERVICE_ERROR.getCode(),
					ZNJFResponseResult.ResultEnum.SERVICE_ERROR.getDesc());
		}
	}
}
