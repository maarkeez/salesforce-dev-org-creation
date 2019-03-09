package com.salesforcedevorgcreation.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class EmailFilterService {

    @Autowired
    private EmailService emailService;


    public String findTextInEmail(EmailFilter emailFilter) throws IOException, MessagingException {
        final Pattern betweenStrPattern = Pattern.compile(emailFilter.getBeforeStr() + ".*" + emailFilter.getAfterStr());

        List<Email> emails = emailService.readEmails();

        log.info("Retrieved emails: {}", emails.size());
        emails.forEach(email -> {
            log.debug("");
            log.debug("--------------------------------------------");
            log.debug("{}", email);
            log.debug("--------------------------------------------");
        });

        log.debug("Filtering emails");

        Optional<Email> verificationEmailOpt = emails.stream()
                .filter(email -> email.getFrom().contains(emailFilter.getFrom()))
                .filter(email -> email.getSubject().contains(emailFilter.getSubject()))
                .filter(containsAllLines(emailFilter.getBodyLines()))
                .findFirst();

        if (!verificationEmailOpt.isPresent()) {
            throw new IllegalStateException("Email not found");
        }

        Matcher m = betweenStrPattern.matcher(verificationEmailOpt.get().getBody());
        String url = "";
        if (m.find()) {
            url = m.group(0).trim(); //is your string. do what you want
            url = url.replace(emailFilter.getBeforeStr(), "");
            url = url.replace(emailFilter.getAfterStr(), "").trim();
        }
        log.info("ExpectedString: '{}'", url);
        if ("".equals(url)) {
            throw new IllegalStateException("Expected string not found");
        }

        return url;

    }

    private Predicate<Email> containsAllLines(List<String> bodyLines) {
        return email -> {
            boolean containsAllLines = true;
            for (String bodyLineExpected : bodyLines) {
                if (!email.getBody().contains(bodyLineExpected)) {
                    containsAllLines = false;
                    break;
                }
            }
            return containsAllLines;
        };
    }
}