package com.pass.service.common.entity;

import java.util.List;

public class LoginUserInfoDTO {
	
	/**
	 * 系统登录id
	 */
	private Long id;
	/**
	 * 用户名称
	 */
	private String userName;
	
	/**
	 * 登录ip
	 */
	private String loginIp;
	
	/**
	 * 管理员权限
	 */
	private Integer isAdmin;
	
	
    private String phone;
	
	/**
	 * 用户站点权限
	 */
	List<String> stationList;
	
	
	/**
	 * 设备列表
	 */
	List<String> deviceList;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public List<String> getStationList() {
		return stationList;
	}
	public void setStationList(List<String> stationList) {
		this.stationList = stationList;
	}
	public Integer getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Integer isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public List<String> getDeviceList() {
		return deviceList;
	}
	public void setDeviceList(List<String> deviceList) {
		this.deviceList = deviceList;
	}


}
