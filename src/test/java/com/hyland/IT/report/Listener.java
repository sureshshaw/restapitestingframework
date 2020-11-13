package com.hyland.IT.report;

import org.apache.commons.lang3.StringUtils;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import java.util.ArrayList;

public class Listener extends TestListenerAdapter {

	private long executionStartTime = 0;
//	private long executionFinishTime = 0;
//	private long totalCompletionTimeForTestSuite = 0;
	private long completionTimeForIndividualTestCase = 0;
	private long testCaseStartTime;
	private long testCaseFinishTime;
	private int testCaseSequence = 1;
	private boolean reportDisplay = false;
	private ArrayList<Report> testResultList = new ArrayList<Report>();

	public void onStart(ITestContext context) {
		executionStartTime = context.getStartDate().getTime();
	}

//	public void onFinish(ITestContext context) {
//		executionFinishTime = context.getEndDate().getTime();
//		System.out.println("executionFinishTime : " + executionFinishTime);
//		totalCompletionTimeForTestSuite = executionFinishTime - executionStartTime;
//		System.out.println("totalCompletionTimeForTestSuite : "+totalCompletionTimeForTestSuite);
//	}

	public void onTestStart(ITestResult result) {
		testCaseStartTime = System.currentTimeMillis();
		//testCaseStartTime = result.getStartMillis();
	}

	public void onTestSuccess(ITestResult result) {
		testCaseFinishTime = System.currentTimeMillis();
		//		testCaseFinishTime = result.getEndMillis();
		this.printTestResults(result);
	}

	public void onTestFailure(ITestResult result) {
		testCaseFinishTime = System.currentTimeMillis();
		//		testCaseFinishTime = result.getEndMillis();
		this.printTestResults(result);
	}

	public void onTestSkipped(ITestResult result) {
		testCaseFinishTime = System.currentTimeMillis();
		//		testCaseFinishTime = result.getEndMillis();
		this.printTestResults(result);
	}

	private void printTestResults(ITestResult result) {

		String testVMName = (String) result.getAttribute("testVMName");
		String fileName = (String) result.getAttribute("fileName");
		String endPoint = (String) result.getAttribute("endPoint");
		String methodType = (String) result.getAttribute("methodType");
		String queryParam = (String) result.getAttribute("queryParam");
		String pathParam = (String) result.getAttribute("pathParam");
		String dbQuery = (String) result.getAttribute("dbQuery");
		Integer totalEndPointCount = (Integer) result.getAttribute("totalEndPointCount");

		if(StringUtils.isNotBlank(queryParam)) {
			queryParam = queryParam.replaceAll("\\{", "").replaceAll("\\}", "");
		}

		if(StringUtils.isNotBlank(pathParam)) {
			pathParam = pathParam.replaceAll("\\{", "").replaceAll("\\}", "");
		}

		completionTimeForIndividualTestCase = testCaseFinishTime - testCaseStartTime;

		String exception = "";
		String status = "";
		switch (result.getStatus()) {
		case 1: {
			status = "SUCCESS";
			break;
		}
		case 2: {
			status = "FAILED";
			exception = result.getThrowable().getMessage();
			break;
		}
		case 3: {
			status = "SKIPED";
			exception = result.getThrowable().getMessage();
			break;
		}
		}

		try {
			Report report = new Report(testCaseSequence, fileName, endPoint, methodType, queryParam, pathParam, dbQuery, completionTimeForIndividualTestCase, status, exception, executionStartTime, totalEndPointCount, testVMName);
			testResultList.add(report);
			ObjectMapper objectMapper = new ObjectMapper();
			//String jsonObj = objectMapper.writeValueAsString(reportL);
			String jsonObj = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(testResultList);
			JSON_Report(jsonObj);

			if (!reportDisplay) {
				File htmlFile = new File("Report/Report.html");
				Desktop.getDesktop().browse(htmlFile.toURI());
				reportDisplay = !reportDisplay;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		++testCaseSequence;
	}

	public static void JSON_Report(String report) {
		try {
			FileWriter fw = new FileWriter("Report/data/datafile.js");
			fw.write("var data = " + report + ";");
			fw.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
