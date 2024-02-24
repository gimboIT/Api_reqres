package tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.UserBody;
import models.UserDataResponse;
import models.UserResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import specs.Specs;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.Specs.request;
import static specs.Specs.response200;
import static tests.TestData.*;
import static tests.TestData.fakeId;

@Feature("User tests")
@Owner("tim")
@Severity(SeverityLevel.BLOCKER)
public class UsersTests {

    @Test
    @DisplayName("Create valid user")
    void createUser() {

        UserBody userData = new UserBody();
        userData.setName(fakeName);
        userData.setJob(fakeJob);

        UserResponse response = given(Specs.request)
                .body(userData)
                .when()
                .post("/users")
                .then()
                .spec(Specs.response201)
                .extract().as(UserResponse.class);

        Assertions.assertThat(response.getName()).isEqualTo(fakeName);
        Assertions.assertThat(response.getJob()).isEqualTo(fakeJob);
    }

    @Test
    @DisplayName("Update user with existed id")
    void updateUser() {

        UserBody userData = new UserBody();
        userData.setName(fakeName);
        userData.setJob(fakeJob);

        UserResponse response = step("Update name of user with id " + fakeId, () ->
                given(request)
                        .body(userData)
                        .when()
                        .put("/users/" + fakeId)
                        .then()
                        .spec(response200)
                        .extract().as(UserResponse.class));

        step("Verify name of updated user with id " + fakeId, () ->
                assertThat(response.getName()).isEqualTo(fakeName));
        assertThat(response.getJob()).isEqualTo(fakeJob);
    }


    @Test
    @DisplayName("Delete user with existed id")
    void deleteUser() {
        step("Delete user with id " + fakeId, () ->
                given()
                        .spec(request)
                        .when()
                        .delete("/users/"+ fakeId)
                        .then()
                        .statusCode(204));
    }

    @Test
    @DisplayName("Get info about existed user")
    void getInfoAboutSingleUser() {

        UserDataResponse responseData = step("Get info about user with id " + testUserId, () ->
                given(request)
                        .when()
                        .get("/users/" + testUserId)
                        .then()
                        .spec(response200)
                        .extract().as(UserDataResponse.class));

        step("Verify user id", () ->
                assertThat(responseData.getData().getId()).isEqualTo(testUserId));

        step("Verify user email", () ->
                assertThat(responseData.getData().getEmail()).isEqualTo("janet.weaver@reqres.in"));

        step("Verify user firstName", () ->
                assertThat(responseData.getData().getFirstName()).isEqualTo("Janet"));

        step("Verify user lastName", () ->
                assertThat(responseData.getData().getLastName()).isEqualTo("Weaver"));

        step("Verify user avatar", () ->
                assertThat(responseData.getData().getAvatar()).isEqualTo("https://reqres.in/img/faces/2-image.jpg"));
    }
}
