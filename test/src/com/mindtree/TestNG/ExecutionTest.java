package com.mindtree.TestNG;

import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.mindtree.libraries.ExcelDrivers;
import com.mindtree.libraries.KeywordUtility;
import com.mindtree.libraries.Utils;

public class ExecutionTest {
	private static KeywordUtility oKwDriver;
	private static ExcelDrivers oExcelDriver;
	private static String sDriverPropertyFile = "./src/inputfile/AutomationInput.properties";
	private static Properties oDriverProperties;
	private static String sInputFileFolder;
	private static String sResultFolder;
	private static String sMainDriverInputFile;
	private static String sCurrentTestCaseStatus;

	@BeforeTest
	public void beforeTest() {

		oDriverProperties = Utils.getProperties(sDriverPropertyFile);

		sInputFileFolder = oDriverProperties.getProperty("InputFileFolder").trim();
		sMainDriverInputFile = oDriverProperties.getProperty("DriverInputFile").trim();
		sResultFolder = oDriverProperties.getProperty("ResultFolder").trim();

	}

	@Test(priority = 0)
	public static void TestSuiteDriver() {

		String sTestCaseSheetName, sRunFlag, sRunStatus, sComment;
		String sDriverExcelFile;
		int iRow, iRowCount;

		SoftAssert s_assert = new SoftAssert();

		sDriverExcelFile = sInputFileFolder + "\\" + sMainDriverInputFile;
		oExcelDriver = new ExcelDrivers();
		oExcelDriver.openExcelWorkbook(sDriverExcelFile);

		iRowCount = oExcelDriver.getRowCountOfSheet("TestSuite");

		for (iRow = 2; iRow <= iRowCount + 1; iRow++) {
			sTestCaseSheetName = "";
			sRunFlag = "";
			sRunStatus = "";
			sComment = "";
			sCurrentTestCaseStatus = "Pass";

			sTestCaseSheetName = oExcelDriver.getCellCData("TestSuite", iRow, 2);
			sRunFlag = oExcelDriver.getCellCData("TestSuite", iRow, 3);

			sTestCaseSheetName = sTestCaseSheetName.trim();

			sRunFlag = sRunFlag.toLowerCase().trim();

			if (sRunFlag.equals("yes")) {
				oKwDriver = null;
				sRunStatus = TestCaseDriver(sTestCaseSheetName);

				if (sRunStatus == "") {
					if (sCurrentTestCaseStatus == "Pass") {
						sRunStatus = "Pass";
					} else {
						sRunStatus = "Fail";
						sComment = "One or more steps got Failed";
					}

				} else {
					sComment = sRunStatus;
					sRunStatus = "Fail";
				}

				s_assert.assertEquals(sRunStatus, "Pass");
				;
			} else {
				sRunStatus = "Skipped";
				sComment = "Because, Run Flag was set to " + sRunFlag;

			}

			oExcelDriver.setCellCData("testSuite", iRow, 4, sRunStatus);
			oExcelDriver.setCellCData("testSuite", iRow, 5, sComment);

		}
		s_assert.assertAll();
	}

	private static String TestCaseDriver(String sSheetName) {
		int iRow, iRowCount;

		String sTestCaseDriverreturnvalue = "";

		String sActionKeyword;
		String sObjectLocator;
		String sArgumentValue;
		String sRunStatus;
		String sComment;
		String sReturnValue;
		By oBy;

		try {

			oKwDriver = new KeywordUtility();
			iRowCount = oExcelDriver.getRowCountOfSheet(sSheetName);
			System.out.println("The row count is: " + iRowCount);

			for (iRow = 2; iRow <= iRowCount + 1; iRow++) {
				sActionKeyword = "";
				sObjectLocator = "";
				sArgumentValue = "";
				sRunStatus = "";
				sComment = "";
				sReturnValue = "";
				oBy = null;

				sActionKeyword = oExcelDriver.getCellCData(sSheetName, iRow, 2).trim();
				sObjectLocator = oExcelDriver.getCellCData(sSheetName, iRow, 3).trim();
				sArgumentValue = oExcelDriver.getCellCData(sSheetName, iRow, 4).trim();

				if (sObjectLocator != "" && !sObjectLocator.equals("")) {
					oBy = Utils.getLocatorBy(sObjectLocator);
				}

				if (sActionKeyword == "") {
					sRunStatus = "Skipped";
					sComment = "No Action Keyword";
				} else {
					try {

						sReturnValue = oKwDriver.performAction(sActionKeyword, oBy, sArgumentValue);

						if (sReturnValue.toLowerCase().contains("error")) {
							sRunStatus = "Exception";
							sComment = sReturnValue;
							sReturnValue = "Fail";
							sCurrentTestCaseStatus = "Fail";
						} else {
							sRunStatus = "keyword executed successfully";
							sReturnValue = "Pass";
						}

					} catch (Exception e) {
						sRunStatus = "Exception";
						sReturnValue = "Fail";
						sComment = e.getMessage();
						sCurrentTestCaseStatus = "Fail";
					}
				}

				oExcelDriver.setCellCData(sSheetName, iRow, 5, sRunStatus);
				oExcelDriver.setCellCData(sSheetName, iRow, 6, sReturnValue);
				oExcelDriver.setCellCData(sSheetName, iRow, 7, sComment);

			}

		} catch (Exception e) {
			sTestCaseDriverreturnvalue = e.getMessage();
			sCurrentTestCaseStatus = "Fail";
		}

		return sTestCaseDriverreturnvalue;
	}

	@Test(priority = 1)
	private static void exportToExcel() {
		String sOutputFileName;
		String sDateTimeStamp;

		sDateTimeStamp = Utils.getDateTimeStamp();

		sOutputFileName = sResultFolder + "\\Result as on " + sDateTimeStamp + ".xlsx";

		boolean status = oExcelDriver.saveAs(sOutputFileName);

		Assert.assertEquals(status, true);
	}

}
