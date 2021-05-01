package com.lg.constant;

import com.baomidou.mybatisplus.extension.api.IErrorCode;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP结果封装
 */
public enum Result implements IErrorCode {

	// 系统模块
	SUCCESS(0, "操作成功"),
	ERROR(1, "操作失败"),
	INFO(2, "请求受理成功，响应数据为空！"),
	UNAUTHENTIC(3, "无权访问，当前是匿名访问，请先登录！"),
	UNAUTHORIZED(4, "无权访问，当前帐号权限不足！"),
	NOTFOUND(5, "服务器未找到资源"),
	BLOCKED(6,"当前请求流量过大"),

	//web
	WEB_400(400, "错误请求"),
	WEB_401(401, "访问未得到授权"),
	WEB_404(404, "资源未找到"),
	WEB_500(500, "服务器内部错误"),
	WEB_UNKOWN(999, "未知错误"),

	//login && access
	LOGIN_FAIL(600, "登录失败"),
	ACCESS_DENIED(601, "无权访问"),
	//parameter
	ARG_TYPE_MISMATCH(1000, "参数类型错误"),
	ARG_BIND_EXCEPTION(1001, "参数绑定错误"),
	ARG_VIOLATION(1002, "参数不符合要求"),
	ARG_MISSING(1003, "参数未找到"),


	;

	private int code = 200;
	private String msg;

	Result(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public long getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() { return msg; }

	public void setMsg(String msg) { this.msg = msg; }

	@Override
	public String toString() {
		return "code:" + code + ";msg:" + msg;
	}

}
