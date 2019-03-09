package com.salesforcedevorgcreation.selenium;

import com.salesforcedevorgcreation.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;

import static org.openqa.selenium.support.ui.ExpectedConditions.urlToBe;

@Slf4j
@Component
class SeleniumRunnerNewDevOrg extends SeleniumRunnerAbstract {

    @Autowired
    private EmailService emailService;

    @Override
    String doAction(WebDriver driver) {

        final String newUser = buildUserName();

        driver.get("https://developer.salesforce.com/signup");

        WebElement firstName = setInputField(driver, "first_name", "Test");
        setInputField(driver, "last_name", "Testing");
        setInputField(driver, "email", emailService.getEmail());
        setSelectField(driver, "job_role", "Developer");
        setInputField(driver, "company", "Testing");
        setSelectField(driver, "country", "ES");
        setInputField(driver, "postal_code", "2890");
        setInputField(driver, "username", newUser);
        setCheckboxField(driver, "eula", true);
        firstName.submit();

        final String successLogin = "https://developer.salesforce.com/signup/success";
        wait(driver, 15).until(urlToBe(successLogin));
        assertUrl(driver, successLogin);

        return newUser;
    }

    private String buildUserName() {
        String[] emailSplited = emailService.getEmail().split("@");
        String userName = String.format("%s-%s@%s", emailSplited[0], dateFormat.format(Calendar.getInstance().getTime()), emailSplited[1]);
        log.info("Sing up with username: {}", userName);
        return userName;
    }

}
