package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.RegistrationDto;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class AuthTest {

    @BeforeAll
    static void setUpAll() {
        Configuration.baseUrl = "http://localhost:9999";
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void openPage() {
        open("/");
    }

    @Test
    void shouldLoginIfUserIsRegisteredAndActive() {
        RegistrationDto user = DataGenerator.getRegisteredUser("active");

        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("button.button").click();

        $("[data-test-id=dashboard]").shouldBe(visible);
    }

    @Test
    void shouldShowErrorIfUserIsRegisteredButBlocked() {
        RegistrationDto user = DataGenerator.getRegisteredUser("blocked");

        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("button.button").click();

        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(exactText("Ошибка! Пользователь заблокирован"));
    }

    @Test
    void shouldShowErrorIfUserIsNotRegistered() {
        RegistrationDto user = DataGenerator.getRandomUser("active");

        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("button.button").click();

        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldShowErrorIfLoginIsWrong() {
        RegistrationDto registeredUser = DataGenerator.getRegisteredUser("active");
        RegistrationDto wrongLoginUser = DataGenerator.getUserWithWrongLogin(registeredUser);

        $("[data-test-id=login] input").setValue(wrongLoginUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongLoginUser.getPassword());
        $("button.button").click();

        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldShowErrorIfPasswordIsWrong() {
        RegistrationDto registeredUser = DataGenerator.getRegisteredUser("active");
        RegistrationDto wrongPasswordUser = DataGenerator.getUserWithWrongPassword(registeredUser);

        $("[data-test-id=login] input").setValue(wrongPasswordUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPasswordUser.getPassword());
        $("button.button").click();

        $("[data-test-id=error-notification]")
                .shouldBe(visible)
                .shouldHave(exactText("Ошибка! Неверно указан логин или пароль"));
    }
}