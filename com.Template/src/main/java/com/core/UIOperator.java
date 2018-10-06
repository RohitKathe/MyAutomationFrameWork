package com.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class UIOperator {
	static Connection conn = null;
	static Statement stmt = null;
	static ResultSet rs = null;

	public boolean ClickElement(String strButtonName) throws Exception {
		try {
			WebElement element = GetObjectFromRepository(strButtonName);
			element.click();
			com.core.Logger.WriteLog("Clicked On Element: " + strButtonName, true, false);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			com.core.Logger.WriteLog("Unable to find element: " + strButtonName, false, true);
			return false;
		}
	}

	public boolean VerifyXPathElementExist(String strXPath) throws Exception {
		try {
			WebElement element = Framework.framework.webDriver.findElement(By.xpath(strXPath));
			if (element.isDisplayed()) {
				com.core.Logger.WriteLog("Found element with XPath :" + strXPath, true, false);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			com.core.Logger.WriteLog("Unable to get Element with XPath : " + strXPath, true, false);
			return false;
		}
	}

	public boolean SelectCheckbox(String strButtonName, boolean btnSelect) throws Exception {
		try {
			WebElement element = GetObjectFromRepository(strButtonName);

			if (element.isSelected() && btnSelect == false) {
				element.click();
				com.core.Logger.WriteLog("Check box CHECK OFF on element: " + strButtonName, true, false);
			}

			if (!element.isSelected() && btnSelect == true) {
				element.click();
				com.core.Logger.WriteLog("Check box CHECK ON on Element: " + strButtonName, true, false);
			}
			return true;
		} catch (Exception e) {
			com.core.Logger.WriteLog("Unable to Select Element: " + strButtonName + "Value to Select" + btnSelect,
					false, true);
			return false;
		}
	}

	public boolean SelectRadio(String strButtonName, String strSelectionText) throws Exception {
		boolean isSelected = false;
		try {
			Dictionary<String, String> oData = GetDataFromExcel("Object_Repostory.xlsx", "Sheet1", strButtonName);
			String strId = GetDataFromExcel("Object_Repository.xlsx", "Sheet1", strButtonName).get("ID");
			java.util.List<WebElement> RadioGroup1 = Framework.framework.webDriver.findElements(By.id(strId));
			for (int i = 0; i < RadioGroup1.size(); i++) {
				if (RadioGroup1.get(i).getAttribute("value").equals(strSelectionText)
						&& RadioGroup1.get(i).isSelected() == false)
					com.core.Logger.WriteLog("Successfully selected Radio button : " + strButtonName
							+ "Value selected as " + strSelectionText, true, true);
				RadioGroup1.get(i).click();
				isSelected = true;
			}
			return isSelected;
		} catch (Exception e) {
			e.printStackTrace();
			com.core.Logger.WriteLog("Unable to Select Element : " + strButtonName + "Value to Select" + strButtonName,
					false, true);
			return isSelected;
		}
	}

	public boolean VerifyControlExist(String strControlName) throws Exception {
		boolean isControlExists = false;
		try {
			WebElement element = GetObjectFromRepository(strControlName);
			if (element.isDisplayed()) {
				com.core.Logger.WriteLog("Verified that the control exists. Control Name : " + strControlName, true,
						false);
				isControlExists = true;
			} else {
				com.core.Logger.WriteLog("The control not exist. Control Name : " + strControlName, false, false);
			}
		} catch (Exception e) {
			com.core.Logger.WriteLog("The control not exist. Control Name : " + strControlName, false, false);
		}
		return isControlExists;
	}

	public boolean ControlNotExist(String strControlName) throws Exception {
		try {
			Webdriverwrapper.SetImplicitTimeOut(2);
			Webdriverwrapper.SetScriptTimeOut(2);
			WebElement element = GetObjectFromRepository(strControlName);
			com.core.Logger.WriteLog(
					"Specified Control exists on page (Not as per Expected). Control Name: " + strControlName, false,
					true);
			return false;
		} catch (Exception e) {
			com.core.Logger.WriteLog(
					"Specified control not exist on Page(as Expected). Control Name : " + strControlName, true, true);
			return true;
		} finally {
			Webdriverwrapper.SetImplicitTimeOut(Integer.parseInt(Framework.framework.GetProperty("IMPLICIT_TIMEOUT")));
			Webdriverwrapper.SetScriptTimeOut(Integer.parseInt(Framework.framework.GetProperty("SCRIPT_TIMEOUT")));
		}
	}

	public boolean CheckControlExistByText(String strText) throws Exception {
		try {
			if (Framework.framework.webDriver.findElement(By.xpath(".//[normalize-space(text())='" + strText + "']"))
					.isDisplayed()) {
				com.core.Logger.WriteLog("Control Exists with text : " + strText, true, false);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			com.core.Logger.WriteLog("Control not Exist with text: " + strText, false, true);
			return false;
		}
	}

	public boolean ClickLinkWithText(String strText) throws Exception {
		try {
			String XPath = "//*[@text='" + strText + "']";
			Framework.framework.webDriver.findElement(By.xpath(XPath)).click();
			com.core.Logger.WriteLog("Successfully clicked on the Link with Text : " + strText, true, true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			com.core.Logger.WriteLog("Unable to click on the Link with text: " + strText, false, true);
			return false;
		}
	}

	public String GetTextFromElement(String strControlName) throws Exception {
		try {
			WebElement element = GetObjectFromRepository(strControlName);
			String strReturn = element.getText().replace("\n", " ");
			return strReturn;
		} catch (Exception e) {
			return "";
		}
	}

	public boolean VerifyControlNotExist(String strControlName) throws Exception {
		try {
			WebElement element = GetObjectFromRepository(strControlName);
			com.core.Logger.WriteLog(
					"Specified Control exists on Page(Not as per Expected). Control Name : " + strControlName, false,
					false);
			return false;
		} catch (Exception e) {
			com.core.Logger.WriteLog(
					"Specified Control not exist on Page(Not as per Expected). Control Name :" + strControlName, true,
					false);
			return true;
		}
	}

	public String GetInnerHtmlFromElement(String strControlName) {
		try {
			WebElement element = GetObjectFromRepository(strControlName);
			String strReturn = element.getAttribute("innerHtml");
			return strReturn;
		} catch (Exception e) {
			return "";
		}
	}

	public boolean EnterText(String strEditName, String strValue) throws Exception {
		try {
			WebElement element = GetObjectFromRepository(strEditName);
			element.clear();
			element.sendKeys(strValue);
			com.core.Logger.WriteLog("Entered value " + strValue + " in Text box: " + strEditName, true, false);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			com.core.Logger.WriteLog("Unable to find element : " + strEditName, false, true);
			return false;
		}
	}

	public boolean SelectCombo(String strSelector, String strValue) throws Exception {
		try {
			WebElement element = GetObjectFromRepository(strSelector);
			Select select = new Select(element);
			select.selectByValue(strValue);
			com.core.Logger.WriteLog("Select Value" + strValue + " in Combo box: " + strSelector, true, false);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			com.core.Logger.WriteLog("Unable to Select : " + strSelector, false, true);
			return false;
		}
	}

	public Dictionary<String, String> GetDataFromExcel(String strFilePath, String strSheetName, String strDataRowName)
			throws IOException {
		Dictionary<String, String> oDict = new Hashtable<String, String>();
		Dictionary<Integer, String> oDictColumnNames = new Hashtable<Integer, String>();

		if (strFilePath.toUpperCase().equals("OBJECT_REPOSITORY>XLSX")) {
			strFilePath = System.getProperty("user.dir") + "\\ObjectRepository\\Object_Repository.xlsx";
			strSheetName = "Object_Repository";
		} else {
			strFilePath = System.getProperty("user.dir") + "\\Test_Data\\"
					+ Framework.framework.GetProperty("TEST_DATA_FILE_NAME");
		}
		FileInputStream oFile = new FileInputStream(new File(strFilePath));
		XSSFWorkbook oworkbook = new XSSFWorkbook(oFile);
		XSSFSheet osheet = oworkbook.getSheet(strSheetName);
		Iterator<Row> rowIterator = osheet.iterator();
		boolean isHeader = true;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			// Take first row as column names and add to dictionary

			if (isHeader == true) {
				Iterator<Cell> cellIterator = row.cellIterator();
				int columnIterator = 0;
				while (cellIterator.hasNext()) {
					columnIterator++;
					Cell cell = cellIterator.next();
					oDict.put(cell.getStringCellValue(), "");
					oDictColumnNames.put(columnIterator, cell.getStringCellValue());
				}
				isHeader = false;
			} else {
				if (row.getCell(0).toString().equals(strDataRowName)) {
					Iterator<Cell> cellIterator = row.cellIterator();
					int columnIterator = 0;
					while (cellIterator.hasNext()) {
						columnIterator++;
						Cell cell = cellIterator.next();
						oDict.put(oDictColumnNames.get((cell.getColumnIndex() + 1)), cell.getStringCellValue());
					}
					break;
				}
			}
		}
		oFile.close();
		oFile = null;
		oDictColumnNames = null;
		return oDict;
	}

	public WebElement GetObjectFromRepository(String strControlName) {
		WebElement oElement = null;
		try {
			Dictionary<String, String> oDict = GetDataFromExcel("Object_Repository.xlsx", "Sheet1", strControlName);
			String strId = "", strName = "", strClassName = "", strXPath = "", strLinkText = "",
					strPartialLinkText = "", strTagName = "", strCSSSelector = "";
			strId = oDict.get("ID");
			strName = oDict.get("NAME");
			strClassName = oDict.get("CLASSNAME");
			strLinkText = oDict.get("LINKTEXT");
			strPartialLinkText = oDict.get("PARTIALLINKTEXT");
			strTagName = oDict.get("TAGNAME");
			strCSSSelector = oDict.get("CSSSELECTOR");
			strXPath = oDict.get("XPATH");

			if (!strId.isEmpty()) {
				oElement = Framework.framework.webDriver.findElement(By.id(strId));
			} else if (!strName.isEmpty()) {
				oElement = Framework.framework.webDriver.findElement(By.name(strName));
			} else if (!strClassName.isEmpty()) {
				oElement = Framework.framework.webDriver.findElement(By.className(strClassName));
			} else if (!strLinkText.isEmpty()) {
				oElement = Framework.framework.webDriver.findElement(By.linkText(strLinkText));
			} else if (!strPartialLinkText.isEmpty()) {
				oElement = Framework.framework.webDriver.findElement(By.partialLinkText(strPartialLinkText));
			} else if (!strTagName.isEmpty()) {
				oElement = Framework.framework.webDriver.findElement(By.tagName(strTagName));
			} else if (!strCSSSelector.isEmpty()) {
				oElement = Framework.framework.webDriver.findElement(By.cssSelector(strCSSSelector));
			} else if (!strXPath.isEmpty()) {
				oElement = Framework.framework.webDriver.findElement(By.xpath(strXPath));
			}
			// rs.close;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			oElement = null;
		} catch (IOException e) {
			e.printStackTrace();
			oElement = null;
		}
		return oElement;
	}

}
