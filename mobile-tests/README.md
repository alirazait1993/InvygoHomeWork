# Invygo Home Assessment
## Getting Started
These instruction will get you a copy of the project up and running on your local machine for analysing my automation skills.
### Prerequisities

- Java SDK 17
- Maven
- Appium

### Running the Test
#### Steps

- run the mvn clean
- run the mvn install
- run the testNGMobile.xml file placed in the mobile-tests module

Note: Login with valid opt require manual interaction to enter OTP from the user
If running the search tests separately user must be logged into the application

### Test Reports
I am using allure reports for recording the test result
you can generate the html result report by navigating to the src/test/resources/allureTestReport/allure-report and running the cmd allure generate