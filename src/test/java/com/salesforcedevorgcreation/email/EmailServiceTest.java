package com.salesforcedevorgcreation.email;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
@Component
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Value("${email.service.username}")
    private String username;

    @Value("${email.service.password}")
    private String password;

    private Pattern p = Pattern.compile("Haga clic en .* para iniciar sesión ahora");

    public String readEmails(String salesforceUserName) throws IOException, MessagingException {
        assertNotNull(username);
        assertNotNull(password);
        List<Email> emails = emailService.readEmails(username, password);

        log.info("Retrieved emails: {}", emails.size());
        emails.forEach(email -> {
            log.info("");
            log.info("--------------------------------------------");
            log.info("{}", email);
            log.info("--------------------------------------------");
        });

        log.info("Filtering emails");

        Email verificationEmail = emails.stream()
                .filter(email -> email.getFrom().contains("developer@salesforce.com"))
                .filter(email -> email.getSubject().contains("Salesforce: Verificar su cuenta"))
                .filter(email -> email.getBody().contains("Le damos la bienvenida a Salesforce Developer Edition"))
                .filter(email -> email.getBody().contains(salesforceUserName))
                .findFirst().get();

        Matcher m = p.matcher(verificationEmail.getBody());
        String url = "";
        if(m.find()) {
            url = m.group(0).trim(); //is your string. do what you want
            url = url.replace("Haga clic en ","");
            url = url.replace(" para iniciar sesión ahora","").trim();
        }
        log.info("Verification url: '{}'", url);
        assertNotEquals("",url);

        return url;

    }
}