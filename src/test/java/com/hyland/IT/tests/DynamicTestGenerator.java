package com.hyland.IT.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hyland.IT.bean.MasterFile;
import com.hyland.IT.bean.TestCases;
import com.hyland.IT.bean.TestModule;
import com.hyland.IT.restUtils.APIUtils;

import io.restassured.http.Cookies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Factory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicTestGenerator {
	
	private static final Logger logger = LogManager.getLogger(DynamicTestGenerator.class);

	@Factory
	public Object[] restAPIFactoryMethod() throws IOException {

		File file = new File("Master.yaml");
		ObjectMapper mapperMasterFile = new ObjectMapper(new YAMLFactory());
		MasterFile masterFile = mapperMasterFile.readValue(file, MasterFile.class);
		List<String> moduleFileNames = masterFile.getFiles();
		logger.debug(moduleFileNames);

		Cookies cookies;
		if(APIUtils.getCookies(masterFile) != null)
			cookies = APIUtils.getCookies(masterFile);
		else
			cookies = null;
		
		Integer totalEndPointCount = 0;
		
		Map<String, TestModule> allModules = new HashMap<String, TestModule>();
		for(String moduleFileName : moduleFileNames) {
			File moduleFile = new File("testcases/" + moduleFileName);
			ObjectMapper mapperModuleFile = new ObjectMapper(new YAMLFactory());
			TestModule testModule = mapperModuleFile.readValue(moduleFile, TestModule.class);
			allModules.put(moduleFileName, testModule);
		}
		
		for(Map.Entry<String, TestModule> entry : allModules.entrySet()) {
			TestModule testModule = entry.getValue();
			for(TestCases moduleTestCase : testModule.getTestCases()) {
				totalEndPointCount++;
			}
		}
		
		List<IntegrationTest> allTestCases = new ArrayList<>();
		
		for(Map.Entry<String, TestModule> entry : allModules.entrySet()) {
			String moduleFile = entry.getKey();
			TestModule testModule = entry.getValue();
			for(TestCases moduleTestCase : testModule.getTestCases()) {
				IntegrationTest integrationTestobj = new IntegrationTest(masterFile, testModule, moduleTestCase, cookies, moduleFile, totalEndPointCount);
				allTestCases.add(integrationTestobj);
			}
		}
		return allTestCases.toArray();			
	}	
}