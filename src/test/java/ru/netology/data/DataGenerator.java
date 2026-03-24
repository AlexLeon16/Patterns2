package ru.netology.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.ContentType;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private DataGenerator() {
    }

    private static final Faker faker = new Faker(new Locale("en"));

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .build();

    public static String randomLogin() {
        return faker.name().username();
    }

    public static String randomPassword() {
        return faker.internet().password();
    }

    public static RegistrationDto getRandomUser(String status) {
        return new RegistrationDto(
                randomLogin(),
                randomPassword(),
                status
        );
    }

    public static RegistrationDto getRegisteredUser(String status) {
        RegistrationDto user = getRandomUser(status);
        register(user);
        return user;
    }

    public static RegistrationDto getUserWithWrongLogin(RegistrationDto registeredUser) {
        return new RegistrationDto(
                randomLogin(),
                registeredUser.getPassword(),
                registeredUser.getStatus()
        );
    }

    public static RegistrationDto getUserWithWrongPassword(RegistrationDto registeredUser) {
        return new RegistrationDto(
                registeredUser.getLogin(),
                randomPassword(),
                registeredUser.getStatus()
        );
    }

    private static void register(RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }
}