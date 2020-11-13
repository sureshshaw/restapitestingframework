package com.hyland.IT.tests;

import io.restassured.RestAssured;
import io.restassured.http.Cookies;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.internal.BaseTestMethod;
import com.hyland.IT.bean.DBQueryArgument;
import com.hyland.IT.bean.DBValidation;
import com.hyland.IT.bean.MasterFile;
import com.hyland.IT.bean.ExpectedResult;
import com.hyland.IT.bean.TestCases;
import com.hyland.IT.bean.TestModule;
import com.hyland.IT.report.Listener;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Listeners(Listener.class)
public class IntegrationTest{

	private static final Logger logger = LogManager.getLogger(IntegrationTest.class);
	private ITestResult result;
	private TestModule testModule;
	private MasterFile masterFile;
	private TestCases testCase;
	private Cookies cookies;
	private String fileName;
	private String dbQuery = "";
	private Integer totalEndPointCount;
	private String testVMName;

	public IntegrationTest(MasterFile masterFile, TestModule testModule, TestCases testCase, Cookies cookies, String fileName, Integer totalEndPointCount) {
		this.masterFile = masterFile;
		this.testModule = testModule;
		this.testCase = testCase;
		this.cookies = cookies;
		this.fileName = fileName;
		this.totalEndPointCount = totalEndPointCount;
	}

	@AfterMethod
	public void setResultTestName(ITestResult result) {
		try {
			BaseTestMethod baseTestMethod = (BaseTestMethod) result.getMethod();
			Field field = baseTestMethod.getClass().getSuperclass().getDeclaredField("m_methodName");
			field.setAccessible(true);
			field.set(baseTestMethod, (result.isSuccess() ? "PASSED --> " : "FAILED --> " ) + this.getTestName());	
			String exception = result.getThrowable().getMessage();
			result.setThrowable(new Throwable(exception));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getTestName() {
		String queryParam = "null";
		String pathParam = "null";

		if(this.testCase.getQueryParams()!=null) {
			queryParam = this.testCase.getQueryParams().toString();
		}

		if(this.testCase.getPathParams()!=null) {
			pathParam = this.testCase.getPathParams().toString();
		}	

		return "File Name : " + this.fileName + "; " +
		"End Point : " + testCase.getMethodType() + 
		"::" + testCase.getEndPoint() + 
		" [Query Params: " + queryParam + ", " +
		" Path Params: " + pathParam + ", " + 
		"DB Query: " + dbQuery + 
		"]";
	}

	@BeforeMethod
	public void beforeTest(ITestResult result){

		try {
			this.result = result;
			testVMName = masterFile.getLoginBaseUrl().split(":")[1].replace("//", "").toUpperCase();
			result.setAttribute("testVMName", this.testVMName);
			result.setAttribute("fileName", this.fileName);
			result.setAttribute("endPoint", this.testCase.getEndPoint());
			result.setAttribute("methodType", this.testCase.getMethodType());
			result.setAttribute("totalEndPointCount", totalEndPointCount);

			if(this.testCase.getQueryParams()!=null) {
				result.setAttribute("queryParam", this.testCase.getQueryParams().toString());
			}
			else{
				result.setAttribute("queryParam", "");	
			}

			if(this.testCase.getPathParams()!=null) {
				result.setAttribute("pathParam", this.testCase.getPathParams().toString());
			}
			else{
				result.setAttribute("pathParam", "");	
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	} 

	@Test
	public void restAPITest() {

		logger.debug("Started : " + fileName + " : " + testCase.getEndPoint());		
		RestAssured.baseURI = testModule.getBaseUrl();
		String endPoint = testCase.getEndPoint();
		RestAssured.basePath = endPoint;

		RequestSpecification request = RestAssured.given();

		Map<String, String> queryParams = testCase.getQueryParams();
		Map<String, String> pathParams = testCase.getPathParams();

		if (queryParams != null) {
			request.queryParams(queryParams);
		}

		if (pathParams != null) {
			request.pathParams(pathParams);
		}

		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", masterFile.getAuthorization());
		headers.put("Content-Type", masterFile.getContentType());
		request.headers(headers);

		if(cookies != null)
			request.cookies(cookies);
		else
			System.exit(0);

		Response response = null;

		String requestBody = testCase.getRequestBody();

		switch (testCase.getMethodType()) {
		case "POST":
			request.body(requestBody);
			//request.body(new File("requestPayload/" + requestBody));
			response = request.request(Method.POST);
			break;
		case "GET":
			response = request.request(Method.GET);
			break;
		}

		if (response != null) {

			SoftAssert softAssert = new SoftAssert();

			ExpectedResult expectedResult = testCase.getExpectedResult();
			if (expectedResult != null) {
				softAssert.assertEquals(response.statusCode(), expectedResult.getStatusCode());
				
				validateResponseTime(response, softAssert, expectedResult);
				
				validateResponseJsonSchema(response, softAssert, expectedResult);
				
				validateHeaders(response, softAssert, expectedResult);

				validateResponseBody(response, softAssert, expectedResult);

				validateDatabase(masterFile, response, softAssert, expectedResult);
			}
			softAssert.assertAll();
		}
		logger.debug("Ended : " + fileName + " : " + testCase.getEndPoint());
	}


	private void validateDatabase(MasterFile masterFile, Response response, SoftAssert softAssert, ExpectedResult expectedResult) {
		if (expectedResult.getDbValidation() != null) {
			DBValidation dbValidation = expectedResult.getDbValidation();
			ResponseBody body = response.getBody();

			try (Connection conn = DriverManager.getConnection(masterFile.getDbURL(), masterFile.getDbUser(), masterFile.getDbPwd())) {

				try (PreparedStatement pst = conn.prepareStatement(dbValidation.getQuery())) {
					dbQuery = dbValidation.getQuery();

					// Preparing sql query arguments
					List<DBQueryArgument> dbQueryArguments = dbValidation.getArguments();
					//List<Object> args = new ArrayList<>();
					if (dbQueryArguments != null) {
						for (int i = 0; i < dbQueryArguments.size(); i++) {
							DBQueryArgument arg = dbQueryArguments.get(i);

							Object val = body.path(arg.getGpath());
							if (val instanceof Integer || val instanceof String) {
								//args.add(val);
								pst.setObject(i + 1, val);
								dbQuery = dbQuery.replaceFirst("\\?", String.valueOf(val));
							} else {
								throw new IllegalArgumentException("Unsupported data type for [gpath : " + arg.getGpath() + ", value : " + val + "]");
							}
						}
					}

					try (ResultSet rs = pst.executeQuery()) {
						result.setAttribute("dbQuery", dbQuery);
						logger.debug("DBQuery : " + dbQuery);
						boolean hasRows = rs.next();
						softAssert.assertTrue(hasRows, "Query should return a row");

						Map<String, String> expectedColumnValues = dbValidation.getExpectedColumnValues();
						if (expectedColumnValues != null && hasRows) {
							for (Map.Entry<String, String> entry : expectedColumnValues.entrySet()) {
								Object colValObj = rs.getObject(entry.getKey());

								if (colValObj instanceof String) {
									String actualVal = (String) colValObj;
									softAssert.assertEquals(("" + actualVal).trim(), entry.getValue().trim());
								} else if (colValObj instanceof Integer) {
									int actualVal = (Integer) colValObj;
									softAssert.assertEquals(actualVal, Integer.parseInt(entry.getValue()));
								} else {
									throw new IllegalArgumentException("Datatype currently not supported");
								}
							}
						}
					}
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void validateResponseBody(Response response, SoftAssert softAssert, ExpectedResult expectedResult) {
		if (expectedResult.getResponseBody() != null) {
			for (Map.Entry<String, String> entryBody : expectedResult.getResponseBody().entrySet()) {
				softAssert.assertEquals(response.getBody().path(entryBody.getKey().toString()), entryBody.getValue());
			}
		}
	}

	private void validateHeaders(Response response, SoftAssert softAssert, ExpectedResult expectedResult) {
		if (expectedResult.getResponseHeaders() != null) {
			for (Map.Entry<String, String> entryHeader : expectedResult.getResponseHeaders().entrySet()) {
				softAssert.assertEquals(response.getHeader(entryHeader.getKey()), entryHeader.getValue());
			}
		}
	}

	private void validateResponseTime(Response response, SoftAssert softAssert, ExpectedResult expectedResult) {
		if (expectedResult.getResponseTime() != null) {
			Long actualResponseTime = response.time()/1000;
			softAssert.assertTrue(actualResponseTime < expectedResult.getResponseTime(), "Expected Response time " + expectedResult.getResponseTime() + " sec but Actual Response Time " + (double)response.time()/1000+" sec. \n\t");			
		}
	}
	
	private void validateResponseJsonSchema(Response response, SoftAssert softAssert, ExpectedResult expectedResult) {
		if (expectedResult.getResponseJsonSchemaFileName() != null) {
			softAssert.assertNotNull(response.then().body(matchesJsonSchema(new File("jsonschema/" + expectedResult.getResponseJsonSchemaFileName()))));
			//response.then().assertThat().body(matchesJsonSchemaInClasspath(expectedResult.getResponseJsonSchemaFileName()));
		}
	}
}