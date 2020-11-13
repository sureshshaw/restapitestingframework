package com.hyland.IT.restUtils;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpResponseException;

import com.hyland.IT.bean.MasterFile;

import io.restassured.RestAssured;
import io.restassured.http.Cookies;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APIUtils {

	public static Cookies getCookies(MasterFile masterFile) throws UnknownHostException, HttpResponseException {
		Cookies cookies = null;
		RestAssured.baseURI = masterFile.getLoginBaseUrl();
		RequestSpecification request = RestAssured.given();

		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Authorization", masterFile.getAuthorization());
		headers.put("TenantId", masterFile.getTenantID());
		request.headers(headers);

		request.queryParam("page", 1);
		Response response = null;
		try {
			response = request.request(Method.GET);
		}
		catch(Exception e) {
			try {
				throw new Exception("Test machine(VM) is not reachable. Please connect to VPN");
			} catch (Exception e1) {
				System.exit(0);
			}
		}
		if (response != null && response.statusCode() == 200 && response.time() != -1 && response.sessionId() != null) {
			cookies = response.getDetailedCookies();
		}
		return cookies;
	}
}
