package com.hyland.IT.bean;

import java.io.Serializable;
import java.util.Map;

public class TestCases implements Serializable
{
	private String endPoint;

	private String methodType;

	private Map<String,String> queryParams;

	private Map<String,String> pathParams;

	private String requestBody;

	private ExpectedResult expectedResult;

	public void setEndPoint(String endPoint){
		this.endPoint = endPoint;
	}
	public String getEndPoint(){
		return this.endPoint;
	}
	public void setMethodType(String methodType){
		this.methodType = methodType;
	}
	public String getMethodType(){
		return this.methodType;
	}

	public Map<String, String> getQueryParams() {
		return queryParams;
	}
	public void setQueryParams(Map<String, String> queryParams) {
		this.queryParams = queryParams;
	}
	public Map<String, String> getPathParams() {
		return pathParams;
	}
	public void setPathParams(Map<String, String> pathParams) {
		this.pathParams = pathParams;
	}
	public void setRequestBody(String requestBody){
		this.requestBody = requestBody;
	}
	public String getRequestBody(){
		return this.requestBody;
	}

	public void setExpectedResult(ExpectedResult expectedResult){
		this.expectedResult = expectedResult;
	}
	public ExpectedResult getExpectedResult(){
		return this.expectedResult;
	}
	
	@Override
	public String toString() {
		return "OCTestCases [endPoint=" + endPoint + ", methodType=" + methodType + ", queryParams=" + queryParams
				+ ", pathParams=" + pathParams + ", requestBody=" + requestBody + ", expectedResult=" + expectedResult
				+ "]";
	}

}


