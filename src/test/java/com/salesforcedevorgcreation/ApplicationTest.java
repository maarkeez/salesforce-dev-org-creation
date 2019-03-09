package com.salesforcedevorgcreation;

import com.salesforcedevorgcreation.email.EmailServiceTest;
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

import static com.salesforcedevorgcreation.selenium.SeleniumService.Type.CHANGE_PASSWORD;
import static com.salesforcedevorgcreation.selenium.SeleniumService.Type.NEW_DEVELOPER_ORG;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTest {
    @Autowired
    private EmailServiceTest emailServiceTest;

    @Autowired
    private SeleniumService seleniumServiceTest;

    @Test
    public void test_Application() throws IOException, MessagingException, InterruptedException {
        String username = seleniumServiceTest
                .buildRunner(NEW_DEVELOPER_ORG)
                .run();


        idle(10);
        String verificationUrl = emailServiceTest.readEmails(username);

        String password = seleniumServiceTest
                .buildRunner(CHANGE_PASSWORD)
                .addProperty(SeleniumRunnerProperties.CHANGE_PASSWORD_URL, verificationUrl)
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
