package com.mindtree.libraries;


import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonDrivers {

	private WebDriver oDriver;
	private long lngPageLoadTimeOut;
	private long lngElementDetectionTimeOut;

	public CommonDrivers() {
		lngPageLoadTimeOut = 60L;
		lngElementDetectionTimeOut = 30L;
	}

	public void setPageLoadTimeOut(long lngPageLoadTimeOut) {
		this.lngPageLoadTimeOut = lngPageLoadTimeOut;
	}

	public void setElementDetectionTimeOut(long lngElementDetectionTimeOut) {
		this.lngElementDetectionTimeOut = lngElementDetectionTimeOut;
	}

	public String openBrowser(String sBrowserType, String sUrl) {
		try {

			switch (getBrowserTypeIndexed(sBrowserType)) {
			case 1:
				oDriver = new FirefoxDriver();
				break;
			case 2:

				System.setProperty("webdriver.ie.driver", "./IEDriverServer.exe");
				oDriver = new InternetExplorerDriver();
				break;

			case 3:
				System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
				oDriver = new ChromeDriver();
				break;
			default:
				throw new Exception("Unknow Browser Type = " + sBrowserType);

			}

			if (sUrl.isEmpty()) {

				sUrl = "about:blank";
			}

			oDriver.manage().window().maximize();
			oDriver.manage().deleteAllCookies();

			oDriver.manage().timeouts().pageLoadTimeout(lngPageLoadTimeOut, TimeUnit.SECONDS);

			oDriver.manage().timeouts().implicitlyWait(lngElementDetectionTimeOut, TimeUnit.SECONDS);

			oDriver.get(sUrl);
			Thread.sleep(2000);
			return "";

		} catch (Exception e) {
			e.printStackTrace();
			return "error:" + e.getMessage();
		}

	}

	public WebDriver getDriver() {

		return oDriver;
	}

	private int getBrowserTypeIndexed(String sBrowserType) {
		sBrowserType = sBrowserType.toLowerCase().trim();

		if (sBrowserType.isEmpty()) {
			return -1;
		}

		if (sBrowserType.equals("ff") || sBrowserType.equals("firefox") || sBrowserType.equals("mozilla")
				|| sBrowserType.equals("mozilla firefox")) {
			return 1;
		}

		if (sBrowserType.equals("ie") || sBrowserType.equals("explorer") || sBrowserType.equals("internet explorer")) {
			return 2;
		}

		if (sBrowserType.equals("chrome") || sBrowserType.equals("google") || sBrowserType.equals("google chrome")) {
			return 3;
		}

		return -1;
	}

	public String waitTillElementIsVisible(By oBy, long timeoutSeconds) {
		try {

			WebDriverWait oWait = new WebDriverWait(oDriver, timeoutSeconds);

			oWait.until(ExpectedConditions.visibilityOfElementLocated(oBy));

			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "error:" + e.getMessage();

		}
	}

	public String savePageSnapshot(String sImagePath) {
		try {
			String sDateTimeStamp;
			sDateTimeStamp = Utils.getDateTimeStamp();
			sImagePath = sImagePath + "_" + sDateTimeStamp + ".jpeg";
			TakesScreenshot oCamera;
			File oTmpFile, oImageFile;
			oImageFile = new File(sImagePath);

			if (oImageFile.exists()) {
				return "error:Image File already Exists";
			}

			oCamera = (TakesScreenshot) oDriver;
			oTmpFile = oCamera.getScreenshotAs(OutputType.FILE);
			oCamera.getScreenshotAs(OutputType.FILE);

			FileUtils.copyFile(oTmpFile, oImageFile);
			return "";

		} catch (Exception e) {
			e.printStackTrace();
			return "error:" + e.getMessage();
		}
	}

	public WebElement findObject(By oBy) {
		WebElement element = null;
		try {
			element = oDriver.findElement(oBy);

		} catch (Exception e) {
			e.printStackTrace();
			return element;
		}

		return element;

	}

}

