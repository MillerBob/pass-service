package com.pass.service.common.exception;

/**
 * 所有业务异常的枚举
 * 
 * @date 2016年11月12日 下午5:04:51
 */
public enum BizExceptionEnum {
	
	/**
	 * 文件上传
	 */
	UPLOAD_ERROR(500,"上传图片出错"),
	
	/**
	 * 文件下载
	 */
	OVER_SIZE_FILE(510,"文件大小超出范围，无法进行相关操作！"),
    FILE_READING_ERROR(511,"文件读取失败，无法进行相关操作！"),
    FILE_NOT_FOUND(512,"文件未找到，无法进行相关操作！"),

	;
	
	
	BizExceptionEnum(int code, String message) {
		this.friendlyCode = code;
		this.friendlyMsg = message;
	}
	
	BizExceptionEnum(int code, String message,String urlPath) {
		this.friendlyCode = code;
		this.friendlyMsg = message;
		this.urlPath = urlPath;
	}

	private int friendlyCode;

	private String friendlyMsg;
	
	private String urlPath;

	public int getCode() {
		return friendlyCode;
	}

	public void setCode(int code) {
		this.friendlyCode = code;
	}

	public String getMessage() {
		return friendlyMsg;
	}

	public void setMessage(String message) {
		this.friendlyMsg = message;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

}
