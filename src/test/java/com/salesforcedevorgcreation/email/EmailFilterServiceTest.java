package com.salesforcedevorgcreation.email;

import org.junit.Test;

import static org.junit.Assert.*;

public class EmailFilterServiceTest {

    @Test
    public void test_Filter(){
        final String emailBody = "\r\n" +
                "Username: dmd.pruebas.pruebas-10-03-2019-014938640@gmail.com\n" +
                "Security token (case-sensitive): 4jEAKuW3a4g33x2zsX11BkrsB\r\n" +
                "\r\n" +
                "For more information on using your security token";


        EmailFilterService service = new EmailFilterService();

        String token = service.filterString(
                EmailFilter.builder()
                .beforeStr("Security token \\(case-sensitive\\):")
                .afterStr("(\\s)*For more")
                .build(),
                Email.builder().body(emailBody).build());

        assertEquals("4jEAKuW3a4g33x2zsX11BkrsB", token);
    }

}