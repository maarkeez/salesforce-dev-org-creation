package com.salesforcedevorgcreation.selenium;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@Component
public class SeleniumServiceTest {

    @Autowired
    private SeleniumService seleniumService;

    @Value("${webdriver.gecko.driver}")
    private String webdriverGeckoDriver;

    public void setUp() {
        assertNotNull(webdriverGeckoDriver);
        System.setProperty("webdriver.gecko.driver", webdriverGeckoDriver);
    }

    public void tearDown() {
        System.clearProperty("webdriver.gecko.driver");
    }

    public String test_registerNewDeveloperOrg() {
        String username = seleniumService.registerNewDeveloperOrg();
        assertNotNull(username);
        assertNotEquals("", username);
        return username;
    }


    public String test_changePassword(String url) {
        String password = seleniumService.changePassword(url);
        assertNotNull(password);
        assertNotEquals("", password);
        return password;

    }


}