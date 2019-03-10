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

        List<Email> emails = emailService.readEmails();

        log.info("Retrieved emails: {}", emails.size());
        emails.forEach(email -> {
            log.debug("");
            log.debug("--------------------------------------------");
            log.debug("{}", email);
            log.debug("--------------------------------------------");
        });

        log.info("Filtering emails");

        Optional<Email> verificationEmailOpt = emails.stream()
                .filter(email -> email.getFrom().contains(emailFilter.getFrom()))
                .filter(email -> email.getSubject().contains(emailFilter.getSubject()))
                .filter(containsAllLines(emailFilter.getBodyLines()))
                .findFirst();

        if (!verificationEmailOpt.isPresent()) {
            throw new IllegalStateException("Email not found");
        }

        String url = filterString(emailFilter, verificationEmailOpt.get());
        log.info("ExpectedString: '{}'", url);
        if ("".equals(url)) {
            throw new IllegalStateException("Expected string not found");
        }

        return url;

    }

    String filterString(EmailFilter emailFilter, Email email) {
        final Pattern betweenStrPattern = Pattern.compile(emailFilter.getBeforeStr() + ".*" + emailFilter.getAfterStr());

        String url = "";
        Matcher m = betweenStrPattern.matcher(email.getBody());
        if (m.find()) {
            url = m.group(0).trim();
            url = Pattern.compile(emailFilter.getBeforeStr()).matcher(url).replaceAll("").trim();
            url = Pattern.compile(emailFilter.getAfterStr()).matcher(url).replaceAll("").trim();
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