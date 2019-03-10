package com.salesforcedevorgcreation.selenium;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;

import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

@Slf4j
@Component
class SeleniumRunnerResetSecurityToken extends SeleniumRunnerAbstract {

    @Override
    String doAction(WebDriver driver) throws MalformedURLException {
        
        String baseUrl = getBaseUrl(driver);
        driver.navigate().to(baseUrl + "/lightning/settings/personal/ResetApiToken/home");

        // Switch to content frame when visible
        String frame = "//iframe[@title='Reset Security Token ~ Salesforce - Developer Edition']";
        wait(driver, 10).until(visibilityOfElementLocated(xpath(frame)));
        driver.switchTo().frame(driver.findElement(xpath(frame)));
        // Reset security token
        driver.findElement(By.xpath("//input[@title='Reset Security Token']")).click();
        return "";

    }

}

