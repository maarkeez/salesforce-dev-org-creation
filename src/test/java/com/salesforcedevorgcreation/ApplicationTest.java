package com.salesforcedevorgcreation;

import com.salesforcedevorgcreation.email.EmailFilter;
import com.salesforcedevorgcreation.email.EmailFilterService;
import com.salesforcedevorgcreation.selenium.SeleniumRunnerProperties;
import com.salesforcedevorgcreation.selenium.SeleniumService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Arrays;

import static com.salesforcedevorgcreation.selenium.SeleniumRunnerProperties.CHANGE_PASSWORD_URL;
import static com.salesforcedevorgcreation.selenium.SeleniumService.Type.*;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTest {
    @Autowired
    private EmailFilterService emailFilterService;

    @Autowired
    private SeleniumService seleniumServiceTest;

    @Test
    public void test_Application() throws Throwable {
        String username = seleniumServiceTest
                .buildRunner(NEW_DEVELOPER_ORG)
                .run();


        idle(10);
        String verificationUrl = emailFilterService.findTextInEmail(
                EmailFilter.builder()
                        .from("developer@salesforce.com")
                        .subject("Salesforce: Verificar su cuenta")
                        .bodyLines(Arrays.asList("Le damos la bienvenida a Salesforce Developer Edition", username))
                        .beforeStr("Haga clic en ")
                        .afterStr(" para iniciar sesión ahora")
                        .build());

        String password = seleniumServiceTest
                .buildRunner(CHANGE_PASSWORD)
                .addProperty(CHANGE_PASSWORD_URL, verificationUrl)
                .run();

        seleniumServiceTest
                .buildRunner(CHANGE_LANGUAGE)
                .run();

        seleniumServiceTest
                .buildRunner(RESET_SECURITY_TOKEN)
                .run();

        idle(5);
        String securityToken = emailFilterService.findTextInEmail(
                EmailFilter.builder()
                        .from("support@emea.salesforce.com")
                        .subject("Your new Salesforce security token")
                        .bodyLines(Arrays.asList("you recently changed your password or requested to reset your security token", username))
                        .beforeStr("Security token \\(case-sensitive\\):")
                        .afterStr("(\\s)*For more")
                        .build());

        log.info("CREATED username: {}, password: {}, token: {}", username, password, securityToken);
    }

    private void idle(int seconds) {

        try {
            Thread.sleep(1000 * seconds);
        } catch (InterruptedException e) {
            log.error("Exception: ", e);
        }

    }
}
