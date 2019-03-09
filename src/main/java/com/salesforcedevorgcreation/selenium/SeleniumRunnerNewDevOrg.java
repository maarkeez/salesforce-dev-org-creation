package com.salesforcedevorgcreation.selenium;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static org.openqa.selenium.support.ui.ExpectedConditions.urlToBe;

@Slf4j
@Component
class SeleniumRunnerNewDevOrg extends SeleniumRunnerAbstract {

    @Override
    String doAction(WebDriver driver) {

        final String newUser = buildUserName();

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

        return newUser;
    }

}
