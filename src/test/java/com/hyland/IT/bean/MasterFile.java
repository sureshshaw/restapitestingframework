package com.hyland.IT.bean;

import java.io.Serializable;
import java.util.List;

public class MasterFile implements Serializable {
	
	String loginBaseUrl;
	String authorization;
	String tenantID;
	String contentType;
	String dbURL;
	String dbUser;
	String dbPwd;

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public String getTenantID() {
		return tenantID;
	}

	public void setTenantID(String tenantID) {
		this.tenantID = tenantID;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getDbURL() {
		return dbURL;
	}

	public void setDbURL(String dbURL) {
		this.dbURL = dbURL;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPwd() {
		return dbPwd;
	}

	public void setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
	}

	public String getLoginBaseUrl() {
		return loginBaseUrl;
	}

	public void setLoginBaseUrl(String loginBaseUrl) {
		this.loginBaseUrl = loginBaseUrl;
	}

	List<String> files;

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "MasterFile [files=" + files + "]";
	}
	
}
