package com.salesforcedevorgcreation;

import com.salesforcedevorgcreation.email.EmailServiceTest;
import com.salesforcedevorgcreation.selenium.SeleniumServiceTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.io.IOException;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTest {
    @Autowired
    private EmailServiceTest emailServiceTest;

    @Autowired
    private SeleniumServiceTest seleniumServiceTest;

    @Test
    public void test_Application() throws IOException, MessagingException, InterruptedException {
        seleniumServiceTest.setUp();
        String username = seleniumServiceTest.test_registerNewDeveloperOrg();
        seleniumServiceTest.tearDown();

        idle(10);
        String verificationUrl = emailServiceTest.readEmails(username);

        seleniumServiceTest.setUp();
        String password = seleniumServiceTest.test_changePassword(verificationUrl);
        seleniumServiceTest.tearDown();


        log.info("CREATED username: {}, password:{}", username, password);
    }

    private void idle(int seconds) {

        try {
            Thread.sleep(1000 * seconds);
        } catch (InterruptedException e) {
            log.error("Exception: ", e);
        }

    }
}
