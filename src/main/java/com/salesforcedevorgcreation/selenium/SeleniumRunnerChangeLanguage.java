package com.salesforcedevorgcreation.selenium;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;

import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

@Slf4j
@Component
class SeleniumRunnerChangeLanguage extends SeleniumRunnerAbstract {

    @Override
    String doAction(WebDriver driver) throws MalformedURLException {

        String baseUrl = getBaseUrl(driver);
        driver.navigate().to(baseUrl + "/lightning/settings/personal/LanguageAndTimeZone/home");

        // Switch to content frame when visible
        String frame = "//iframe[@title='Idioma y zona horaria ~ Salesforce - Developer Edition']";
        wait(driver, 10).until(visibilityOfElementLocated(xpath(frame)));
        driver.switchTo().frame(driver.findElement(xpath(frame)));
        // Select English
        setSelectField(driver, "LanguageAndTimeZoneSetup:editPage:editPageBlock:languageLocaleKey", "1");
        // Save
        clickButton(driver, "LanguageAndTimeZoneSetup:editPage:editPageBlock:j_id48:save");

        // Wait until saved
        driver.navigate().refresh();
        frame = "//iframe[@title='Language And Time Zone ~ Salesforce - Developer Edition']";
        wait(driver, 10).until(visibilityOfElementLocated(xpath(frame)));
        return "";

    }

}

