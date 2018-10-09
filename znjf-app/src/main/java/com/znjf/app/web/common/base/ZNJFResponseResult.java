package com.znjf.app.web.common.base;

/**
 * 中南金服App服务端-响应结果
 * @author FTD
 * */
public class ZNJFResponseResult {

	public enum ResultEnum {
		
		SUCCESS(0, "操作成功"), 
        PARAM_INCOMPLETE(10001, "参数不全"),
        OPERATE_ERROR(10002, "系统操作异常"), 
        NET_ERROR(10003, "网络异常"), 
        OPERATE_TIMEOUT(10004, "操作超时"),
        USER_NOTCORRRECT(10005, "用户名或者密码错误"),
        REDIS_OPERATE_ERROR(10006, "登录超时，请重新登录"),
        NO_USERTOKEN(10007, "登录超时，请重新登录"),
        USERTOKEN_FAILED(10009, "登录超时，请重新登录"),
        REQUEST_NEED(10008, "request对象不存在"),
        USERTOKEN_DISABLE(10010, "登录超时，请重新登录"),
        DEVICE_ERROR(10011, "设备信息不全"), 
        PARAM_ERROR(10012, "参数错误"),
        BANK_STORAGE(10013, "请开通银行存管账户"),
        SEND_FREQUENTLY(10016, "短信验证码下发过于频繁"),
        USER_EXIST(10017, "该用户已经存在"),
        VERCODE_ERROR(10018, "请输入正确的短信验证码"),
        REGIST_ERROR(10019, "注册失败"),
        NOT_MOBILE(10020,"请输入正确格式的手机号"),
        ILLEGAL_PASSWORD(10021, "请输入正确格式的密码"),
        USER_NOTEXIST(10022, "登录超时"),
        USER_STATUS_FAIL(10023, "您的账号已被禁用，如有疑问请联系客服"),
        MOBILE_NOT_SAME(10024, "原手机号不正确"),
        IDCARD_NOT_SAME(10025, "身份证号不正确"),
        EMAIL_INVALIDATE(10026, "请输入正确格式的邮箱"),
        PWD_ERROR(10027, "原密码错误"),
        PWD_ERROR_FIVE(10028, "账户被锁定，请找回密码或明日再试"),
        MECHINE_NOT_FIND(10029, "未找到机器号"),
        USER_BANKCARD_ERROR(10030, "不存在用户银行卡记录"),
        FUND_DEL(10031, "该账户已经销户"),
        VERCODE_NOT_EXIST(10032, "验证码失效"),
        MOBILE_HAS_EXIST(10033, "手机号已经存在"),
        PROJECT_NOT_EXIST(10034, "项目不存在"),
        EXCHANGE_FAIL(10035, "兑换失败"),
        PROJECT_STATUS_ERROR(10036, "该项目已融资结束"),
        FUND_NOT_EXIST(10037, "该账户不存在"),
        DEVICE_SEND_TIMES(10038, "该设备下发短信次数达到上限"),
        INDUSTRY_CANNOT_INVEST(10039, "企业用户不能投资"),
        USER_IS_INDUSTRY(10040, "企业用户请至电脑端官网操作"),
        LOGINOTHER(10041,"该账号已在其他设备上登录"),
        USER_FROZEN(10042, "您的账号已被禁用，如有疑问请联系客服"),
        NAME_T0O_LONG(10043, "请输入正确格式的姓名"),
        ACCOUNT_FROZEN(10044, "资金账户被冻结，如有疑问请联系客服"),
        COUPON_NOT_CORRECT(10045, "投资金额与该优惠券规则不符"),
        NOT_NEW(10046,"已有投资行为，不可投资新手福利项目"),
        INOT_SMAE(10047, "该项目为您的融资项目，不可投资"),
        ERROR_PBC_USER_SETTLEMENT_INV_BATCH_INVENTMENT(3010, "给投资人批量结算失败"),
        ERROR_PBC_NOT_SUPPORT(3011, "不支持该操作"), 
        ERROR_PBC_USER_TRADE_INVENTMENT_EXCESS(3012, "投资超过募集额度"),
        BASEINFO_NOT_EXIST(10047, "手机号或密码错误"),
        NOT_UNION(10048, "尚未绑定过"),
        GO_TO_PASSWORD(10049, "暂不是中南金服会员"),
        METHOD_UNABLE(-10000,"该方法暂不可用，请联系客服"),
        RED_NOT_EXIST(10051, "推荐人号码不存在"),
        RED_NOT_VALIDATE(10052, "推荐人号码格式不正确"),
        OUT_BANG_FAIL(10053,"解绑失败"),
        HAS_UNION(10055, "该账号已经绑定过"),
        PROJECT_NOT_START(10050, "该项目暂未开投"),
        NOT_FOUND_RECORD(10060, "未找到记录"),
        NOT_FULL_EIGHTEEN(10061, "根据相关法律规定，未满18周岁不可投资"),
        INVEST_AMOUNT_ERROR(10062, "投资金额不满足条件"),
        IS_OPEN_CG(10070, "已开通成功，请勿重复提交"),
        SERVICE_ERROR(10080, "接口调用失败"),
        RECHARGE_LIMIT_ERROR(10090, "今日提现次数已达上限");
		
		private int code;
		private String desc;
		private ResultEnum(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
}
