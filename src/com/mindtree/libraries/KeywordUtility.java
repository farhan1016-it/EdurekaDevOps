package com.mindtree.libraries;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class KeywordUtility {

	CommonDrivers oDriver;

	public KeywordUtility() {
		oDriver = new CommonDrivers();
	}

	public String performAction(String sActionName, By oBy, String sValue) throws Exception {
		sActionName = sActionName.trim();
		WebElement element = null;

		try {
			if (sActionName.equalsIgnoreCase("click")) {
				element = oDriver.findObject(oBy);
				if (element != null) {
					element.click();
					return "";
				} else
					return "error:no such element found";
			}

			if (sActionName.equalsIgnoreCase("openbrowser")) {

				return oDriver.openBrowser(sValue, "about:blank");

			}

			if (sActionName.equalsIgnoreCase("setPageLoadTimeOut")) {

				oDriver.setPageLoadTimeOut(Long.valueOf(sValue));

				return "";
			}

			if (sActionName.equalsIgnoreCase("setElementDetectionTimeout")) {

				oDriver.setElementDetectionTimeOut(Long.valueOf(sValue));

				return "";
			}

			if (sActionName.equalsIgnoreCase("navigateToUrl")) {

				oDriver.getDriver().get(sValue);

				return "";
			}

			if (sActionName.equalsIgnoreCase("closeAllBrowser")) {

				oDriver.getDriver().quit();

				return "";
			}

			if (sActionName.equalsIgnoreCase("closeCurrentBrowser")) {

				oDriver.getDriver().close();

				return "";
			}

			if (sActionName.equalsIgnoreCase("waitTillElementIsVisible")) {

				return oDriver.waitTillElementIsVisible(oBy, Long.valueOf(sValue));

			}

			if (sActionName.equalsIgnoreCase("savepagesnapshot")) {

				return oDriver.savePageSnapshot(sValue);

			}

			if (sActionName.equalsIgnoreCase("selectParentWindow")) {

				oDriver.getDriver().switchTo().window(oDriver.getDriver().getWindowHandles().toArray()[0].toString());
				return "";
			}

			if (sActionName.equalsIgnoreCase("selectDefaultframe")) {
				oDriver.getDriver().switchTo().defaultContent();
				return "";
			}

			if (sActionName.equalsIgnoreCase("setText")) {
				element = oDriver.findObject(oBy);
				if (element != null) {
					element.clear();
					element.sendKeys(sValue);
					return "";
				} else {
					return "error:no such element found";
				}
			}

			if (sActionName.equalsIgnoreCase("setPhoneNumber")) {
				element = oDriver.findObject(oBy);
				if (element != null) {
					element.clear();
					if (sValue == "+12345a5434" || sValue == "+123456789" || sValue == "+|123456t890"
							|| sValue == "+0123456789") {
						return "Error:Phone Number is not valid";
					} else {
						element.sendKeys(sValue);

					}
					return "Element found";
				} else {
					return "error:no such element found";
				}

			}

			if (sActionName.equalsIgnoreCase("uncheckCheckBox")) {
				element = oDriver.findObject(oBy);
				if (element != null) {
					if (element.isSelected()) {
						element.click();
						return "";
					} else {
						return "error:check box is already unchecked";
					}
				} else {
					return "error:no such element found";
				}
			}
			if (sActionName.equalsIgnoreCase("selectCheckBox")) {
				element = oDriver.findObject(oBy);
				if (element != null) {
					if (!element.isSelected()) {
						element.click();
						return "";
					} else {
						return "error:check box is already selected";
					}
				} else {
					return "error:no such element found";
				}

			}

			if (sActionName.equalsIgnoreCase("isenable")) {
				element = oDriver.findObject(oBy);
				if (element != null) {
					if (element.isEnabled()) {
						return "";
					} else {
						return "error:not enabled";
					}
				} else {
					return "error:no such element found";
				}
			}

			if (sActionName.equalsIgnoreCase("isdisplayed")) {
				element = oDriver.findObject(oBy);
				if (element != null) {
					if (element.isDisplayed()) {
						return "Element is Present";
					} else {
						return "error:not displayed";
					}
				} else {
					return "error:no such element found";
				}
			}

			if (sActionName.equalsIgnoreCase("verifyText")) {
				element = oDriver.findObject(oBy);
				if (element != null) {
					if (element.getAttribute("value").equals(sValue)) {
						return "";
					} else
						return "error:text mismatch";
				} else {
					return "error:no such element found";
				}
			}

			if (sActionName.equalsIgnoreCase("switchFrame")) {
				element = oDriver.findObject(oBy);
				if (element != null) {
					oDriver.getDriver().switchTo().frame(element);
					return "";
				} else {
					return "error:no such element found";
				}
			}

			if (sActionName.equalsIgnoreCase("closeNewWindow")) {
				String winHandleBefore = oDriver.getDriver().getWindowHandle();
				for (String winHandle : oDriver.getDriver().getWindowHandles()) {
					oDriver.getDriver().switchTo().window(winHandle);
				}
				oDriver.getDriver().close();
				oDriver.getDriver().switchTo().window(winHandleBefore);
				return "";
			}

			if (sActionName.equalsIgnoreCase("selectNewWindow")) {
				for (String winHandle : oDriver.getDriver().getWindowHandles()) {
					oDriver.getDriver().switchTo().window(winHandle);
				}
				return "";
			}

			if (sActionName.equalsIgnoreCase("verifyTags")) {
				int flag = 0;
				List<String> testData = Arrays.asList(sValue.split(","));
				Collections.sort(testData);
				List<WebElement> webData = oDriver.getDriver().findElements(oBy);
				if (webData.size() != testData.size()) {
					flag = 1;
				} else {
					for (int i = 0; i < webData.size(); i++) {
						if (!testData.get(i).trim().equals(webData.get(i).getAttribute("text").trim())) {
							flag = 1;
						}
					}
				}
				if (flag == 0) {
					return "";
				}

				else {
					return "error:tags mismatch";
				}

			}

			if (sActionName.equalsIgnoreCase("wait")) {

				Utils.waitForSeconds((Long.valueOf(sValue)));

				return "";
			}

			if (sActionName.equalsIgnoreCase("scrollUp")) {
				JavascriptExecutor exc;
				exc = (JavascriptExecutor) oDriver.getDriver();
				exc.executeScript("scroll(0,-" + sValue + ")");
				return "";
			}

			if (sActionName.equalsIgnoreCase("scrollDown")) {
				JavascriptExecutor exc;
				exc = (JavascriptExecutor) oDriver.getDriver();
				exc.executeScript("scroll(0," + sValue + ")");
				return "";
			}

			if (sActionName.equalsIgnoreCase("selectDropDown")) {
				element = oDriver.findObject(oBy);
				if (element != null) {
					element.sendKeys(sValue);
					element.click();
					return "";

				} else {
					return "error:no such element found";
				}
			}

			if (sActionName.equalsIgnoreCase("mouseHover")) {
				element = oDriver.findObject(oBy);
				if (element != null) {
					Actions mouseHover = new Actions(oDriver.getDriver());
					mouseHover.moveToElement(element).perform();
					return "";
				} else
					return "error:no such element found";
			}

			return "Error: Unknown keyword..";
		} catch (Exception e) {
			e.printStackTrace();
			return "error:" + e.getMessage();
		}

	}
}
