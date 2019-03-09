package com.salesforcedevorgcreation.selenium;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SeleniumService {

    @Autowired
    private SeleniumRunnerNewDevOrg newUserRunner;

    @Autowired
    private SeleniumRunnerChangePassword changePasswordRunner;

    public enum Type {
        NEW_DEVELOPER_ORG, CHANGE_PASSWORD
    }

    public SeleniumRunner buildRunner(Type type) {
        switch (type) {
            case NEW_DEVELOPER_ORG:
                return newUserRunner;
            case CHANGE_PASSWORD:
                return changePasswordRunner;
            default:
                throw new IllegalArgumentException("Invalid type");
        }
    }

}
