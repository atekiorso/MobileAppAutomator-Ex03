import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;

public class MainTest {
    private final long defaultTimeoutInSeconds = 5;
    private AndroidDriver<AndroidElement> driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "8.0");
        capabilities.setCapability("deviceName", "AndroidTestDevice");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "G:\\github\\MobileAppAutomator\\MobileAppAutomator-Ex03\\apks\\org.wikipedia.apk");

        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void cancelSearch() {
        final String searchText = "Java";
        final String searchXpath = "//*[@text='Search Wikipedia']";
        final String searchInputId = "org.wikipedia:id/search_src_text";
        final String titleXpath = "//*[@resource-id='org.wikipedia:id/page_list_item_title']";

        // Тапаем по элементу поиска для открытия формы поиска
        waitForElementAndClick(By.xpath(searchXpath));

        // Вводим в поле ввода строку для поиска
        waitForElementAndSendKeys(By.id(searchInputId), searchText);

        // Проверяем, что результаты поиска содержат несколько статей
        List<WebElement> searchResultElements = waitForElementsPresent(By.xpath(titleXpath));
        Assert.assertTrue("The number of found articles is not more than 1!",
                searchResultElements.size() > 1);

        // Очищаем поле ввода
        waitForElementAndClear(By.id(searchInputId));

        // Проверяем отсутствие результатов поиска
        Assert.assertTrue("The found articles are still present!",
                waitForElementNotPresent(By.xpath(titleXpath)));
    }

    private WebElement waitForElementAndClick(By by) {
        return waitForElementAndClick(by, defaultTimeoutInSeconds);
    }

    private WebElement waitForElementAndClick(By by, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, timeoutInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(By by, String charSequences) {
        return waitForElementAndSendKeys(by, charSequences, defaultTimeoutInSeconds);
    }

    private WebElement waitForElementAndSendKeys(By by, String charSequences, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, timeoutInSeconds);
        element.sendKeys(charSequences);

        return element;
    }

    private WebElement waitForElementAndClear(By by) {
        return waitForElementAndClear(by, defaultTimeoutInSeconds);
    }

    private WebElement waitForElementAndClear(By by, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, timeoutInSeconds);
        element.clear();
        return element;
    }

    private WebElement waitForElementPresent(By by, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage("Element \"" + by + "\" not found!\n");

        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private boolean waitForElementNotPresent(By by) {
        return waitForElementNotPresent(by, defaultTimeoutInSeconds);
    }

    private boolean waitForElementNotPresent(By by, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage("Element \"" + by + "\" is still present!\n");

        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    private List<WebElement> waitForElementsPresent(By by) {
        return waitForElementsPresent(by, defaultTimeoutInSeconds);
    }

    private List<WebElement> waitForElementsPresent(By by, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage("Elements \"" + by + "\" not found!\n");

        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }
}
