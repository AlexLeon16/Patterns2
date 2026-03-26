package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;
import ru.netology.data.RegistrationDto;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class AuthTest {

    @BeforeEach
    void openPage() {
        open("http://localhost:9999");
    }

    @Test
    void shouldLoginIfUserIsRegisteredAndActive() {
        RegistrationDto user = DataGenerator.getRegisteredUser("active");

        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("button.button").click();

        $("h2")
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(text("Личный кабинет"));
    }

    @Test
    void shouldShowErrorIfUserIsRegisteredButBlocked() {
        RegistrationDto user = DataGenerator.getRegisteredUser("blocked");

        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("button.button").click();

        $("[data-test-id=error-notification]")
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(text("Ошибка"))
                .shouldHave(text("Пользователь заблокирован"));
    }

    @Test
    void shouldShowErrorIfUserIsNotRegistered() {
        RegistrationDto user = DataGenerator.getRandomUser("active");

        $("[data-test-id=login] input").setValue(user.getLogin());
        $("[data-test-id=password] input").setValue(user.getPassword());
        $("button.button").click();

        $("[data-test-id=error-notification]")
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(text("Ошибка"))
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldShowErrorIfLoginIsWrong() {
        RegistrationDto registeredUser = DataGenerator.getRegisteredUser("active");
        RegistrationDto wrongLoginUser = DataGenerator.getUserWithWrongLogin(registeredUser);

        $("[data-test-id=login] input").setValue(wrongLoginUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongLoginUser.getPassword());
        $("button.button").click();

        $("[data-test-id=error-notification]")
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(text("Ошибка"))
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldShowErrorIfPasswordIsWrong() {
        RegistrationDto registeredUser = DataGenerator.getRegisteredUser("active");
        RegistrationDto wrongPasswordUser = DataGenerator.getUserWithWrongPassword(registeredUser);

        $("[data-test-id=login] input").setValue(wrongPasswordUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPasswordUser.getPassword());
        $("button.button").click();

        $("[data-test-id=error-notification]")
                .shouldBe(visible, Duration.ofSeconds(10))
                .shouldHave(text("Ошибка"))
                .shouldHave(text("Неверно указан логин или пароль"));
    }
}