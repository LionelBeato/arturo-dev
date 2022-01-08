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

    WebDriver driver;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setupTest() {
        driver = new ChromeDriver();
    }

    @AfterEach void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // searches "hello world" on google
    @Test
    void googleTest() {
        driver.get("http://google.com");
        String searchPath = "/html/body/div[1]/div[3]/form/div[1]/div[1]/div[1]/div[2]/div[2]/input";
        String submitPath = "/html/body/div[1]/div[3]/form/div[1]/div[1]/div[3]/center/input[1]";
        WebElement searchElement = driver.findElement(By.xpath(searchPath));
        WebElement submitElement = driver.findElement(By.xpath(submitPath));
        searchElement.sendKeys("hello world!");
        submitElement.submit();

        var actual = driver.getCurrentUrl();

        assertTrue(actual.contains("hello+world"));
    }

    //*[@id="app"]/nui-app-frame/div/div/nui-login//div/div/div/div[1]/nui-textfield[2]

    @Test
    void dematicTest() {
        driver.get("https://ds-dev.dematic.com/");
        // find shadow root and drill down to input field
        String mainPath = "//*[@id=\"app\"]/nui-app-frame/div/div/nui-login";
        WebElement domainField = driver.findElement(By.xpath(mainPath))
                .getShadowRoot()
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("nui-textfield"))
                .getShadowRoot()
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("label"))
                .findElement(By.cssSelector("input"));

        String js = "arguments[0].setAttribute('value','testing...')";
       ((JavascriptExecutor) driver).executeScript(js, domainField);


        WebElement ssoLogin = driver.findElement(By.xpath(mainPath))
                .getShadowRoot()
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("div"))
                .findElement(By.cssSelector("div.login-group"))
                .findElement(By.cssSelector("nui-button[icon]"));
//                .getShadowRoot()
//                .findElement(By.cssSelector("div"))
//                .findElement(By.cssSelector("button"));



//                .getShadowRoot()
//                .findElement(By.cssSelector(".nui-button"))
//                .findElement(By.cssSelector("button"));


        ssoLogin.click();
        System.out.println(ssoLogin.getAttribute("class"));
        System.out.println(domainField.getAttribute("value"));


    }

    public WebElement expandRootElement(WebElement element) {
        return (WebElement) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].shadowRoot",element);
    }
}
