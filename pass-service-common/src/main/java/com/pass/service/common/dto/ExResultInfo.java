package com.pass.service.common.dto;

import java.io.Serializable;

public class ExResultInfo implements Serializable {
	/** 
     *  
     */
	private static final long serialVersionUID = 1L;
	protected int code; // 结果码
	protected String msg; // 结果详情

	public ExResultInfo() {
	}

	public ExResultInfo(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ExResultInfo [code=" + code + ", msg=" + msg + "]";
	}

}
