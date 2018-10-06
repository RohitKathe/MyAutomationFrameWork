package com.core;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class Webdriverwrapper {

	public static boolean LoadWebDriver() {
		try {
			if (Framework.framework.GetProperty("BROWSERTYPE").toUpperCase().equals("CHROME")) {
				Runtime.getRuntime().exec("taskkill/F/IM chrome.exe");
				String strChromeDriverPath = System.getProperty("user.dir") + "\\resources\\chromedriver.exe";
				System.setProperty("webdriver.chrome.driver", strChromeDriverPath);
				Framework.framework.webDriver = new ChromeDriver();
			} else if (Framework.framework.GetProperty("BROWSERTYPE").toUpperCase().equals("IE")) {
				Runtime.getRuntime().exec("taskkill/F/IM iexplore.exe");
				String strDriverPath = System.getProperty("user.dir") + "\\resources\\IEDriverServer.exe";
				System.setProperty("webdriver.ie.driver", strDriverPath);
				Framework.framework.webDriver = new InternetExplorerDriver();
			} else if (Framework.framework.GetProperty("BROWSERTYPE").toUpperCase().equals("MOBILE_TESTING")) {
				FirefoxProfile profile = new FirefoxProfile();
				profile.setPreference("general.useragent.override", Framework.framework.GetProperty("USER-AGENT"));
				Framework.framework.webDriver = new FirefoxDriver(profile);
			} else if (Framework.framework.GetProperty("BROWSERTYPE").toUpperCase().equals("MOBILE")) {

			} else {
				Runtime.getRuntime().exec("taskkill/F/IM firefox.exe");
				Framework.framework.webDriver = new FirefoxDriver();
			}
			// Set default time out for Webdriver

			if (!Framework.framework.GetProperty("BROWSERTYPE").equals("MOBILE")) {
				SetPageLoadTimeOut(Long.parseLong(Framework.framework.GetProperty("PAGE_LOAD_TIMEOUT")));
				SetImplicitTimeOut(Long.parseLong(Framework.framework.GetProperty("IMPLICIT_TIMEOUT")));
				SetScriptTimeOut(Long.parseLong(Framework.framework.GetProperty("SCRIPT_TIMEOUT")));
			}
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public static void SetPageLoadTimeOut(long intValue) {
		Framework.framework.webDriver.manage().timeouts().pageLoadTimeout(intValue, TimeUnit.SECONDS);
	}

	public static void SetImplicitTimeOut(long intValue) {
		Framework.framework.webDriver.manage().timeouts().implicitlyWait(intValue, TimeUnit.SECONDS);
	}

	public static void SetScriptTimeOut(long intValue) {
		Framework.framework.webDriver.manage().timeouts().setScriptTimeout(intValue, TimeUnit.SECONDS);
	}

	public static String CaptureScreenShot(){
		//Create a file in the Logs folder with current date if it doesn't exist
		//Take a screenshot based on timestamp and return it
		DateFormat format = new SimpleDateFormat("dd_MMMM_YY");
		Date dateobj = new Date();
		String strDirName = format.format(dateobj);
		File file = new File(System.getProperty("user.dir") + "\\Logs\\" + strDirName);
				if (!file.exists()) {
		file.mkdir();	
		}
				format = new SimpleDateFormat("HH-mm-ss");
				dateobj = new Date();
				String strFileName = format.format(dateobj) + ".png";
				String strCompleteFileName =System.getProperty("user.dir")+ "\\Logs\\" + strDirName + "\\" + strFileName;
				File scrFile = ((TakesScreenshot)Framework.framework.webDriver).getScreenshotAs(OutputType.FILE);
				try {
					org.apache.commons.io.FileUtils.copyFile(scrFile, new File(strCompleteFileName));
				} catch (IOException e) {
					System.out.println(e.getMessage());
					strCompleteFileName = "";
				}
		String strWebLink = " <a href=\"" + strCompleteFileName + "\" target =\"_blank\">Click Here For Snapshot</a>";
		return strWebLink;

	}
}
