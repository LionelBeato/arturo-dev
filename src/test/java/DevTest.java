import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

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
        // find shadow root and drill down to input field
        // here we use css selectors as its the most compatible way of accessing
        // shadow root elements
        WebElement domainField = driver.findElement(By.xpath(mainPath))
                .getShadowRoot()// first access †o shadow root
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("nui-textfield"))
                .getShadowRoot() // access to nested shadow root
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("label"))
                .findElement(By.cssSelector("input"));

        // script execution in order to manipulate the input's value
        String js = "arguments[0].setAttribute('value','testing...')";
       ((JavascriptExecutor) driver).executeScript(js, domainField);


       // webelement representing sso login button
        WebElement ssoLogin = driver.findElement(By.xpath(mainPath))
                .getShadowRoot()
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("div.login-group"))
                .findElement(By.cssSelector("nui-button[icon]"));


        ssoLogin.click();
        System.out.println(ssoLogin.getAttribute("class"));
        System.out.println(domainField.getAttribute("value"));

    }

    // abandoned root expander method via js execution
//    public WebElement expandRootElement(WebElement element) {
//        return (WebElement) ((JavascriptExecutor) driver)
//                .executeScript("return arguments[0].shadowRoot",element);
//    }
}
