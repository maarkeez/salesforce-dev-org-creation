package com.salesforcedevorgcreation.selenium;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import static com.salesforcedevorgcreation.selenium.SeleniumRunnerProperties.CHANGE_PASSWORD_URL;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.urlToBe;

@Slf4j
@Component
class SeleniumRunnerChangePassword extends SeleniumRunnerAbstract {

    @Override
    String doAction(WebDriver driver) {

        final String password = buildPassword();

        // And now use this to visit Google
        driver.get((String) getProperties().get(CHANGE_PASSWORD_URL));
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

}
