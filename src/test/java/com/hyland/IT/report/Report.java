package com.hyland.IT.report;

public class Report {

	public int testCaseSequence;
	public String fileName;
	public String endPoint;
	public String methodType;
	public String queryParam;
	public String pathParam;
	public String dbQuery;
	public long completionTimeForIndividualTestCase;
	public String status;
	public String exception;
	public long executionStartTime;
	public Integer totalEndPointCount;
	public String testVMName;
	//public long totalCompletionTimeForTestSuite;
	
	public Report(int testCaseSequence, String fileName, String endPoint, String methodType, String queryParam, String pathParam, String dbQuery,
			long completionTimeForIndividualTestCase, String status, String exception, long executionStartTime, Integer totalEndPointCount, String testVMName) {
		this.testCaseSequence = testCaseSequence;
		this.fileName = fileName;
		this.endPoint = endPoint;
		this.methodType = methodType;
		this.queryParam = queryParam;
		this.pathParam = pathParam;
		this.dbQuery = dbQuery;
		this.completionTimeForIndividualTestCase = completionTimeForIndividualTestCase;
		this.status = status;
		this.exception = exception;
		this.executionStartTime = executionStartTime;
		this.totalEndPointCount = totalEndPointCount;
		this.testVMName = testVMName;
		//this.totalCompletionTimeForTestSuite = totalCompletionTimeForTestSuite;
	}
}
