package service;

import error.DuplicatedUserException;
import error.FailedLoginException;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserServiceTest {
    private UserService userService = UserService.getInstance();

    @Test
    void signUp(){
        userService = UserService.getInstance();
        userService.signUp("aelley", "1234", "im", "abcd@naver.com");
    }

    @Test
    void signUpWithDuplicatedId(){
        savedUser();
        userService = UserService.getInstance();
        String expectedErrorMessage = "이미 존재하는 user 입니다.";
        DuplicatedUserException duplicatedUserException = Assertions.assertThrows(DuplicatedUserException.class,
                () -> userService.signUp("kelley", "123", "kim", "abc@naver.com"));
        String message = duplicatedUserException.getMessage();
        Assertions.assertEquals(expectedErrorMessage, message);
    }

    @Test
    void login(){
        userService = UserService.getInstance();
        User signedUpUser = User.of("kelley", "123", "kim", "abc@naver.com");
        UserService.getInstance().login(signedUpUser.getUserId(), signedUpUser.getPassword());
    }

    @Test
    void loginWithInvalidUser(){
        String expectedErrorMessage = "id나 password 를 잘못 입력하셨습니다";
        User invalidUser = User.of("kelley1", "123", "kim", "abc@naver.com");

        FailedLoginException failedLoginException = Assertions.assertThrows(FailedLoginException.class,
                () -> userService.login(invalidUser.getUserId(), invalidUser.getPassword()));

        String message = failedLoginException.getMessage();
        Assertions.assertEquals(expectedErrorMessage, message);
    }

    void savedUser(){
        userService = UserService.getInstance();
        userService.signUp("kelley", "123", "kim", "abc@naver.com");
    }

}