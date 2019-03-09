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
import static com.salesforcedevorgcreation.selenium.SeleniumService.Type.CHANGE_PASSWORD;
import static com.salesforcedevorgcreation.selenium.SeleniumService.Type.NEW_DEVELOPER_ORG;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTest {
    @Autowired
    private EmailFilterService emailFilterService;

    @Autowired
    private SeleniumService seleniumServiceTest;

    @Test
    public void test_Application() throws IOException, MessagingException, InterruptedException {
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

        log.info("CREATED username: {}, password: {}", username, password);
    }

    private void idle(int seconds) {

        try {
            Thread.sleep(1000 * seconds);
        } catch (InterruptedException e) {
            log.error("Exception: ", e);
        }

    }
}
