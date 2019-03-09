package com.salesforcedevorgcreation.selenium;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.openqa.selenium.By.id;

@Slf4j
abstract class SeleniumRunnerAbstract implements SeleniumRunner {

    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HHmmssSSS");
    @Getter(AccessLevel.PACKAGE)
    private Map<SeleniumRunnerProperties, Object> properties = new HashMap<>();

    @Value("${webdriver.gecko.driver}")
    private String webdriverGeckoDriver;

    @Override
    public String run() {
        System.setProperty("webdriver.gecko.driver", webdriverGeckoDriver);

        WebDriver driver = buildDriver();
        String output = doAction(driver);
        driver.quit();

        System.clearProperty("webdriver.gecko.driver");

        return output;
    }

    abstract String doAction(WebDriver driver);

    @Override
    public SeleniumRunner addProperty(SeleniumRunnerProperties key, Object value) {
        properties.put(key, value);
        return this;
    }

    void assertUrl(WebDriver driver, String successLogin) {
        if (!driver.getCurrentUrl().equals(successLogin)) {
            throw new IllegalStateException("Login not successful");
        }
    }

    void clickButton(WebDriver driver, String id) {
        driver.findElement(id(id)).click();
    }

    String buildPassword() {
        String password = RandomStringUtils.random(16, true, true);
        log.info("Generated password: {}", password);
        return password;
    }



    WebDriverWait wait(WebDriver driver, int timeOutInSeconds) {
        return new WebDriverWait(driver, timeOutInSeconds);
    }

    WebElement setInputField(WebDriver driver, String id, String value) {
        WebElement element = driver.findElement(id(id));
        element.sendKeys(value);
        return element;
    }

    WebElement setSelectField(WebDriver driver, String id, String value) {
        WebElement select = driver.findElement(id(id));

        select.findElements(By.tagName("option"))
                .stream()
                .filter(hasValue(value))
                .findFirst()
                .ifPresent(selectOption());

        return select;
    }


    WebElement setCheckboxField(WebDriver driver, String id, boolean select) {
        WebElement checkbox = driver.findElement(id(id));
        if (select != checkbox.isSelected()) {
            checkbox.click();
        }
        return checkbox;
    }

    private Consumer<? super WebElement> selectOption() {
        return option -> option.click();
    }

    private Predicate<? super WebElement> hasValue(String value) {
        return option -> value.equals(option.getAttribute("value"));
    }

    private WebDriver buildDriver() {
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--headless");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        return new FirefoxDriver(firefoxOptions);
    }
}
