import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

/**
 * Selenium driver util class. Use it to interact with current browser.
 *
 * Dependencies: `selenium/*.jar, selenium/lib/*.jar`
 */
public class DkSeleniums {
	/**
	 * Set text to given element.
	 *
	 * @param element Web element
	 * @param text Text to set
	 */
	public static void setText(WebElement element, String text) {
		element.clear();
		element.click();
		element.sendKeys(text);
	}

	/**
	 * Select dropdown by given index.
	 *
	 * @param element Web element
	 * @param index Index inside pulldown
	 * @throws Exception Some exception
	 */
	public static void selectDropdownByIndex(WebElement element, int index) throws Exception {
		new Select(element).selectByIndex(index);
	}

	/**
	 * Select dropdown by given index.
	 *
	 * @param element Web element
	 * @param value Value inside pulldown
	 * @throws Exception Some exception
	 */
	public static void selectDropdownByValue(WebElement element, String value) throws Exception {
		new Select(element).selectByValue(value);
	}

	/**
	 * Select dropdown by given index.
	 *
	 * @param element Web element
	 * @param text Display-text inside pulldown
	 * @throws Exception Some exception
	 */
	public static void selectDropdownByVisibleText(WebElement element, String text) throws Exception {
		new Select(element).selectByVisibleText(text);
	}

	/**
	 * Click to given web element.
	 *
	 * @param element Web element
	 * @throws Exception Something
	 */
	public static void clickTo(WebElement element) throws Exception {
		element.click();
	}

	/**
	 * Close current running browser.
	 *
	 * @param driver Web driver
	 */
	public static void closeBrowser(WebDriver driver) {
		driver.close();
	}

	public static byte[] takeScreenShot(WebDriver driver) throws Exception {
		if (driver instanceof TakesScreenshot) {
			return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		}
		throw new Exception("Driver does not support take screen shot");
	}

	public static File takeScreenShotAsFile(WebDriver driver) throws Exception {
		if (driver instanceof TakesScreenshot) {
			return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		}
		throw new Exception("Driver does not support take screen shot");
	}

	public static byte[] takeScreenShotAsBytes(WebDriver driver) throws Exception {
		if (driver instanceof TakesScreenshot) {
			return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		}
		else {
			throw new Exception("Driver does not support take screen shot");
		}
	}

	/**
	 * Get browser size.
	 *
	 * @param driver Web driver
	 * @return Size of browser (not web)
	 */
	public static Dimension getBrowserSize(WebDriver driver) {
		return driver.manage().window().getSize();
	}

	public static void clickCenterElementWithOffset(WebDriver driver, WebElement target, int xOffset, int yOffset) {
		Actions actions = new Actions(driver);
		actions.moveToElement(target).moveByOffset(xOffset, yOffset).click().perform();
	}

	public static WebElement waitElementUntilVisible(WebDriver driver, WebElement element, int maxWaitSeconds) {
		return new WebDriverWait(driver, maxWaitSeconds).until(ExpectedConditions.visibilityOf(element));
	}

	public static String getInputText(WebElement element) {
		return element.getAttribute("value");
	}

	public static void fullscreen(WebDriver driver) {
		driver.manage().window().fullscreen();
	}

	public static Alert getCurrentActiveAlert(WebDriver driver) {
		return driver.switchTo().alert();
	}
}
