package tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.RegisterResponseModel;
import models.RegisterUserBody;
import models.RegisterUserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.Specs.*;
import static tests.TestData.fakeEmail;
import static tests.TestData.fakePassword;

@Feature("Register tests")
@Owner("tim")
@Severity(SeverityLevel.BLOCKER)
public class RegisterTests {

    @DisplayName("Positive register")
    @Test
    void regPositiveTest() {
        RegisterUserBody regData = new RegisterUserBody();
        regData.setEmail("eve.holt@reqres.in");
        regData.setPassword(fakePassword);

        RegisterResponseModel response = step("Register successful", () -> given()
                .spec(request)
                .when()
                .body(regData)
                .post("/register")
                .then()
                .spec(response200)
                .extract().as(RegisterResponseModel.class));

        step("check id", () ->
                assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4"));
        step("check token", () ->
                assertThat(response.getId()).isEqualTo("4"));


    }

    @Test
    @DisplayName("Register user without password")
    void registerMissingPasswordTest() {

        RegisterUserBody registerData = new RegisterUserBody();
        registerData.setEmail(fakeEmail);

        RegisterUserResponse response = step("Register user without password", () ->
                given(request)
                        .body(registerData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(response400)
                        .extract().as(RegisterUserResponse.class));

        step("Check displayed error", () ->
                assertThat(response.getError().equals("Missing password")));
    }

    @Test
    @DisplayName("Register user without email")
    void registerMissingEmailTest() {

        RegisterUserBody registerData = new RegisterUserBody();
        registerData.setPassword(fakePassword);

        RegisterUserResponse response = step("Register user without email", () ->
                given(request)
                        .body(registerData)
                        .when()
                        .post("/register")
                        .then()
                        .spec(response400)
                        .extract().as(RegisterUserResponse.class));

        step("Check displayed error", () ->
                assertThat(response.getError().equals("Missing email")));
    }
}
