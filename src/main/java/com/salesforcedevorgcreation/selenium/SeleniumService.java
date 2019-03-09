package com.salesforcedevorgcreation.selenium;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.openqa.selenium.By.id;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.urlToBe;

@Slf4j
@Service
public class SeleniumService {

    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HHmmssSSS");

    public String registerNewDeveloperOrg() {

        final String newUser = buildUserName();

        WebDriver driver =  buildDriver();

        driver.get("https://developer.salesforce.com/signup");

        WebElement firstName = setInputField(driver, "first_name", "DMD");
        setInputField(driver, "last_name", "Testing");
        setInputField(driver, "email", "dmd.pruebas.pruebas@gmail.com");
        setSelectField(driver, "job_role", "Developer");
        setInputField(driver, "company", "Testing");
        setSelectField(driver, "country", "ES");
        setInputField(driver, "postal_code", "28904");
        setInputField(driver, "username", newUser);
        setCheckboxField(driver, "eula", true);
        firstName.submit();


        final String successLogin = "https://developer.salesforce.com/signup/success";
        wait(driver, 15).until(urlToBe(successLogin));
        assertUrl(driver, successLogin);

        //Close the browser
        driver.quit();
        return newUser;
    }

    private void assertUrl(WebDriver driver, String successLogin) {
        if (!driver.getCurrentUrl().equals(successLogin)) {
            throw new IllegalStateException("Login not successful");
        }
    }

    public String changePassword(String changePasswordUrl) {

        final String password = buildPassword();

        // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface,
        // not the implementation.
        WebDriver driver = buildDriver();

        // And now use this to visit Google
        driver.get(changePasswordUrl);
        wait(driver, 15).until(elementToBeClickable(id("newpassword")));
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");
        WebElement firstFormField = setInputField(driver, "newpassword", password);
        setInputField(driver, "confirmpassword", password);
        setInputField(driver, "answer", "madrid");
        clickButton(driver, "password-button");

        final String homeUrl = "https://eu19.lightning.force.com/lightning/setup/SetupOneHome/home";
        wait(driver, 15).until(urlToBe(homeUrl));
        assertUrl(driver, homeUrl);

        //Close the browser
        driver.quit();
        return password;
    }

    private void clickButton(WebDriver driver, String id) {
        driver.findElement(id(id)).click();
    }

    private String buildPassword() {
        String password = RandomStringUtils.random(16, true, true);
        log.info("Generated password: {}", password);
        return password;
    }

    private String buildUserName() {
        String userName = String.format("dmd.pruebas.pruebas-%s@gmail.com", dateFormat.format(Calendar.getInstance().getTime()));
        log.info("Sing up with username: {}", userName);
        return userName;
    }

    private WebDriverWait wait(WebDriver driver, int timeOutInSeconds) {
        return new WebDriverWait(driver, timeOutInSeconds);
    }

    private WebElement setInputField(WebDriver driver, String id, String value) {
        WebElement element = driver.findElement(id(id));
        element.sendKeys(value);
        return element;
    }

    private WebElement setSelectField(WebDriver driver, String id, String value) {
        WebElement select = driver.findElement(id(id));

        select.findElements(By.tagName("option"))
                .stream()
                .filter(hasValue(value))
                .findFirst()
                .ifPresent(selectOption());

        return select;
    }


    private WebElement setCheckboxField(WebDriver driver, String id, boolean select) {
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

    private WebDriver buildDriver(){
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--headless");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        return new FirefoxDriver(firefoxOptions);
    }
}
