package com.salesforcedevorgcreation.selenium;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Service
public class SeleniumService {

    @Autowired
    private SeleniumRunnerNewDevOrg newUserRunner;

    @Autowired
    private SeleniumRunnerChangePassword changePasswordRunner;

    @Autowired
    private SeleniumRunnerChangeLanguage changeLanguageRunner;

    @Autowired
    private SeleniumRunnerResetSecurityToken resetSecurityTokenRunner;

    @Value("${webdriver.gecko.driver}")
    private String webdriverGeckoDriver;

    @Value("${selenium.browser.show:false}")
    private boolean showBrowser;

    @Getter(AccessLevel.PACKAGE)
    private WebDriver driver;

    @PostConstruct
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", webdriverGeckoDriver);
        driver = buildDriver();
    }

    @PreDestroy
    public void tearDown() {
        driver.quit();
        System.clearProperty("webdriver.gecko.driver");
    }

    public enum Type {
        NEW_DEVELOPER_ORG, CHANGE_PASSWORD, CHANGE_LANGUAGE, RESET_SECURITY_TOKEN
    }

    public SeleniumRunner buildRunner(Type type) {
        switch (type) {
            case NEW_DEVELOPER_ORG:
                return newUserRunner;
            case CHANGE_PASSWORD:
                return changePasswordRunner;
            case CHANGE_LANGUAGE:
                return changeLanguageRunner;
            case RESET_SECURITY_TOKEN:
                return resetSecurityTokenRunner;
            default:
                throw new IllegalArgumentException("Invalid type");
        }
    }

    private WebDriver buildDriver() {
        if (showBrowser) {
            return new FirefoxDriver();
        }
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--headless");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        return new FirefoxDriver(firefoxOptions);
    }
}
