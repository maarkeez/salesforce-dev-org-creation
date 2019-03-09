package com.salesforcedevorgcreation.email;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class EmailFilter {
    private String from;
    private String subject;
    private List<String> bodyLines;
    private String beforeStr;
    private String afterStr;
}
