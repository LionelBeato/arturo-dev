import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DevTest {

    // property representing the chromedriver
    WebDriver driver;

    // sets up the chrome driver before all running tests
    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    // initializes the driver variable before each test
    @BeforeEach
    public void setupTest() {
        driver = new ChromeDriver();
    }

    // quits the chrome driver after each test
    @AfterEach void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // searches "hello world" on google
    @Test
    void googleTest() {
        // goes to google.com via chrome driver
        driver.get("http://google.com");
        // varying paths to the search input and submit button
        String searchPath = "/html/body/div[1]/div[3]/form/div[1]/div[1]/div[1]/div[2]/div[2]/input";
        String submitPath = "/html/body/div[1]/div[3]/form/div[1]/div[1]/div[3]/center/input[1]";
        // element variables representing input and submit button
        WebElement searchElement = driver.findElement(By.xpath(searchPath));
        WebElement submitElement = driver.findElement(By.xpath(submitPath));
        // sending inputs to the search input
        searchElement.sendKeys("hello world!");
        submitElement.submit();

        var actual = driver.getCurrentUrl();

        assertTrue(actual.contains("hello+world"));
    }


    @Test
    void dematicTest() {
        // goes to dematic website
        driver.get("https://ds-dev.dematic.com/");
        // path representing the first major element we use to to start drilling down to the shadow root
        String mainPath = "//*[@id=\"app\"]/nui-app-frame/div/div/nui-login";
        // Element representing domain text field
        WebElement domainField = expandRootElement(driver.findElement(By.xpath(mainPath)),
                "querySelectorAll(\"nui-textfield\")[1]"); // using querySelectorAll to retrieve all textfields and selecting the second one in the array
        // in the manner that we accessed it, we are able to interact with our domain field and send keys
        domainField.sendKeys("testing ^_^");

        // webelement representing sso login button
        // note how we use cssselectors for best compatibility
        WebElement ssoLogin = driver.findElement(By.xpath(mainPath))
                .getShadowRoot()
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("div.login-group"))
                .findElement(By.cssSelector("nui-button[icon]"));

        // clicking on SSO login button
        ssoLogin.click();

        // checking values of attributes on elements
        System.out.println(ssoLogin.getAttribute("class"));
        System.out.println(domainField.getAttribute("value"));

    }
    // customized root expander method via js execution
    public WebElement expandRootElement(WebElement element, String additionalScript) {
        return (WebElement) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].shadowRoot." + additionalScript,element);
    }

    public WebElement expandRootElement(WebElement element) {
        return (WebElement) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].shadowRoot",element);
    }
}
