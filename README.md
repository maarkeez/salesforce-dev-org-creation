# Salesforce dev-org creator

This project automate the creation of a new [Salesforce Developer Organization Creation](https://developer.salesforce.com/signup)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

In order to be able to develop on this project, you will need to install:

* [Java jdk 1.8.0](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or above

* Java development IDE

* [Maven 3.6.0](https://maven.apache.org/download.cgi)  or above


### Installing

Follow this steps to install to get a development environment running

* Clone the repository

```
git clone https://github.com/maarkeez/salesforce-dev-org-creation.git
```

* Import the project as Maven project to your Java development IDE.
* Build the project

```
mvn clean install
```

* Add the [application.properties](./src/main/resources/application-template.properties) to the project with your custom configuration.

## Running the tests

There is a [basic test](./src/test/java/com/salesforcedevorgcreation/ApplicationTest.java) which run all the steps to create the new organization.

```
mvn verify
```

You can also automate the test running following the [.gitlab-ci.yml](.gitlab-ci.yml) script instructions

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [Selenium](https://www.seleniumhq.org) - Browser automation
* [Gmail](https://www.google.com/intl/es/gmail/about/) - Email provider

## Authors

* **David Márquez Delgado** - *Initial work* 

## License

This project is licensed under the Apache License - see the [LICENSE.md](LICENSE.md) file for details
