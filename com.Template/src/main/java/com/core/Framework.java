package com.core;

import java.io.FileInputStream;
import java.util.Properties;

import org.openqa.selenium.WebDriver;

import com.google.common.base.StandardSystemProperty;

public class Framework {
	public static Framework framework;
	public WebDriver webDriver;
	private Properties propArray;
	public UIOperator UI;
	public static boolean isTestPassOrFail = true;
	public String strCurrentWindowHandle;
	public String strCurrentScenarioName;

	private Framework() throws Exception {
		// Load Configuration from config file

		if (!LoadConfig()) {
			throw new Exception("Unable to Load Configuration from config file");

		}
	}

	/**
	 * CreateInstance : Function to create instance of framework
	 * 
	 * @return : FrameWork object
	 */
	public static Framework CreateInstance(String strCurrentScenarioName) throws Exception {

		if (Framework.framework == null) {
			Framework.framework = new Framework();

			// Load web driver (based on configuration provided

			if (!Webdriverwrapper.LoadWebDriver()) {
				throw new Exception("Unable to Load Web Driver from Resources");
			}
			// Create UI Operator

			framework.strCurrentScenarioName = strCurrentScenarioName;
			framework.UI = new UIOperator();
		}

		if (Framework.framework.webDriver == null) {
			Webdriverwrapper.LoadWebDriver();
		}
		return Framework.framework;
	}

	/**
	 * LoadConfig : Function to Load configuration file in properties
	 * 
	 * @return : Returns true if Configuration is loaded successfully else false
	 */
	public boolean LoadConfig() {
		propArray = new Properties();
		String strConfigPath = System.getProperty("user.dir") + "\\Config\\Environment.properties";
		System.out.println("Picked up configuration path as" + strConfigPath);
		try {
			FileInputStream oFis = new FileInputStream(strConfigPath);
			propArray.load(oFis);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * GetProperty :  To get value of property using property name
	 * @param strPropertyName : Property name for which value to invoke
	 * @return : Property value
	 */
	
	public String GetProperty(String strPropertyName){
	return propArray.getProperty(strPropertyName)	;
	}
}
