package voa_functional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class taxCalcTest{
	

public static WebDriver driver = null;
public Properties prop = null;

/*@BeforeTest
public void loadproperties(){
	File file = new File("C:/Users/lava/workspace/VOA_Test/src/voa_functional/OR.properties");
	  
	FileInputStream fileInput = null;
	try {
		fileInput = new FileInputStream(file);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	Properties prop = new Properties();
	
	//load properties file
	try {
		prop.load(fileInput);
	} catch (IOException e) {
		e.printStackTrace();
	}
}
*/
	public String StringFormat(String value) {
		String convertedValue = new String(); 
		
		try {
			Double valueDouble = Double.valueOf(value);
			
			if(valueDouble.longValue() == valueDouble) {
				convertedValue = "" + valueDouble.longValue();
			} else {
				convertedValue = "" + valueDouble;
			}
		} catch(NumberFormatException ex) {
			// value is a string, can't be converted to double
			convertedValue = value;
		}
		
		return convertedValue;
	}

  @Test(dataProvider="getdata")
  public void CalcTax(String taxyear,String amntbefore,String taxpaid,String interest,String taxoninterest,String notTaxed,String giftaid,String resulttxt
) throws InterruptedException {
	  

		
		if(driver==null){
			
			driver=new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
		    
		}
		File file = new File("C:\\Users\\lava\\workspace\\VOA_Test\\src\\voa_functional\\OR.properties");
		  
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties prop = new Properties();
		
		//load properties file
		try {
			prop.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}
		driver.get("http://www.hmrc.gov.uk/calcs/stc.htm");
		driver.findElement(By.xpath(prop.getProperty("taxcheckerlink"))).click();
		driver.findElement(By.xpath(prop.getProperty("taxyear"))).sendKeys(taxyear);
		driver.findElement(By.xpath(prop.getProperty("amntbeforetax"))).clear();
		driver.findElement(By.xpath(prop.getProperty("amntbeforetax"))).sendKeys(StringFormat(amntbefore));
		driver.findElement(By.xpath(prop.getProperty("taxpaid"))).clear();
		driver.findElement(By.xpath(prop.getProperty("taxpaid"))).sendKeys(StringFormat(taxpaid));
		driver.findElement(By.xpath(prop.getProperty("Interestpaid"))).clear();
		driver.findElement(By.xpath(prop.getProperty("Interestpaid"))).sendKeys(StringFormat(interest));
		driver.findElement(By.xpath(prop.getProperty("taxonInterest"))).clear();
		driver.findElement(By.xpath(prop.getProperty("taxonInterest"))).sendKeys(StringFormat(taxoninterest));
		driver.findElement(By.xpath(prop.getProperty("notTaxed"))).clear();
		driver.findElement(By.xpath(prop.getProperty("notTaxed"))).sendKeys(StringFormat(notTaxed));
		driver.findElement(By.xpath(prop.getProperty("giftaid"))).clear();
		driver.findElement(By.xpath(prop.getProperty("giftaid"))).sendKeys(StringFormat(giftaid));
		if(!driver.findElement(By.xpath(prop.getProperty("blindallowance_no"))).isSelected()){
			driver.findElement(By.xpath(prop.getProperty("blindallowance_no"))).click();
		}
		driver.findElement(By.xpath(prop.getProperty("Calc"))).click();
		Thread.sleep(2000L);
		
			try{
				String bodyText = driver.findElement(By.tagName("body")).getText();
				Assert.assertTrue(bodyText.contains(resulttxt), "Text not found!");
			
			}
			
			catch(AssertionError er){
				
				System.out.println("assertion error occurred");
				er.printStackTrace();
				Assert.fail();
			}
		}
		
  
  
  @DataProvider
	public Object[][] getdata() {
	  Xls_Reader xlreader = new Xls_Reader("C:\\Users\\lava\\workspace\\VOA_Test\\taxCal_Data.xlsx");
		int rows = xlreader.getRowCount("Sheet1")-1;
		int cols = xlreader.getColumnCount("Sheet1");

          Object[][] data = new Object[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				data[i][j] = xlreader.getCellData("Sheet1", j, i+2);
			}
		}

		return data;
	}	




		
		
	  



}
