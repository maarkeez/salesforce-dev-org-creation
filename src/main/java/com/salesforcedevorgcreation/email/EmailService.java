package com.salesforcedevorgcreation.email;

import com.salesforcedevorgcreation.exception.RuntimeExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class EmailService {

    @Value("${email.service.username}")
    private String username;

    @Value("${email.service.password}")
    private String password;

    private static final String GMAIL_POP_HOST = "pop.gmail.com";

    private Properties getPOPProperties() {
        Properties properties = new Properties();
        properties.put("mail.pop3.host", GMAIL_POP_HOST);
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");
        return properties;
    }

    private Authenticator getAuthenticator(String user, String credentials) {

        Authenticator authenticator = new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, credentials);
            }
        };
        return authenticator;
    }

    public List<Email> readEmails() throws MessagingException, IOException {

        Session session = Session.getInstance(getPOPProperties(), getAuthenticator(username, password));
        Store store = session.getStore("pop3s");
        store.connect(GMAIL_POP_HOST, username, password);

        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);

        List<Email> emails = Arrays.stream(emailFolder.getMessages()).map(RuntimeExceptionHandler.handle(message ->

                Email.builder()
                        .subject(message.getSubject())
                        .from(getAddressList(message.getFrom()).get(0))
                        .to(getAddressList(message.getAllRecipients()))
                        .body(getMessageBody(message))
                        .date(message.getSentDate())
                        .build())

        ).collect(toList());

        emailFolder.close(false);
        store.close();

        return emails;

    }

    private List<String> getAddressList(Address[] addresses) throws MessagingException {

        if (addresses == null) return Collections.emptyList();
        return Arrays.stream(addresses)
                .filter(Objects::nonNull)
                .map(Objects::toString)
                .filter(s -> !Strings.isBlank(s))
                .collect(toList());
    }


    private String getMessageBody(Message message) throws IOException, MessagingException {
        if (message.getContent() instanceof MimeMultipart) {
            return getTextFromMimeMultipart((MimeMultipart) message.getContent());
        } else {
            return message.getContent().toString();
        }
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }



}