package com.salesforcedevorgcreation.selenium;

public interface SeleniumRunner {
    SeleniumRunner addProperty(SeleniumRunnerProperties key, Object value);
    String run() throws Throwable;
}
