package com.example.jeddit.serviciesTests;

import com.example.jeddit.JedditApplication;
import com.example.jeddit.exceptions.DataNotFoundException;
import com.example.jeddit.exceptions.NotCorrectDataException;
import com.example.jeddit.exceptions.NotUniqueDataException;
import com.example.jeddit.exceptions.WrongPasswordException;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.auth.UserRegistrationRequest;
import com.example.jeddit.models.models.auth.UserSignInRequest;
import com.example.jeddit.repositories.UserRepository;
import com.example.jeddit.servicies.impl.AuthServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JedditApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AuthTests {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void registrationUser_Positive() throws NotUniqueDataException, NotCorrectDataException {
        String login = "testlogin";
        String password = "testPass";
        String email = "test@mail.ru";
        UserRegistrationRequest request = new UserRegistrationRequest(login, password, email);
        User expectedUser = new User();
        expectedUser.setLogin(login);
        expectedUser.setPassword(password);
        expectedUser.setEmail(email);

        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        when(authService.registrationUser(request)).thenReturn(expectedUser);

        User resultUser = authService.registrationUser(request);

        assertNotNull(resultUser);
        assertEquals(expectedUser, resultUser);
    }

    @Test
    public void registrationUser_loginLessThen_throwsException() throws NotUniqueDataException, NotCorrectDataException {
        UserRegistrationRequest request = new UserRegistrationRequest("te", "testPass", "test@mail.ru");
        assertThrows(NotCorrectDataException.class, () -> authService.registrationUser(request));
    }

    @Test
    public void registrationUser_loginMoreThen_throwsException() throws NotUniqueDataException, NotCorrectDataException {
        StringBuilder sb = new StringBuilder();
        sb.append("t".repeat(51));

        UserRegistrationRequest request = new UserRegistrationRequest(sb.toString(), "testPass", "test@mail.ru");
        assertThrows(NotCorrectDataException.class, () -> authService.registrationUser(request));
    }

    @Test
    public void registrationUser_loginMustContainOnlyOneWord_throwsException() throws NotUniqueDataException, NotCorrectDataException {
        UserRegistrationRequest request = new UserRegistrationRequest("te st", "testPass", "test@mail.ru");
        assertThrows(NotCorrectDataException.class, () -> authService.registrationUser(request));
    }

    @Test
    public void registrationUser_passwordMoreThen_throwsException() throws NotUniqueDataException, NotCorrectDataException {
        StringBuilder sb = new StringBuilder();
        sb.append("t".repeat(256));

        UserRegistrationRequest request = new UserRegistrationRequest("testLogin", sb.toString(), "test@mail.ru");
        assertThrows(NotCorrectDataException.class, () -> authService.registrationUser(request));
    }

    @Test
    public void registrationUser_emailMoreThen_throwsException() throws NotUniqueDataException, NotCorrectDataException {
        StringBuilder sb = new StringBuilder();
        sb.append("t".repeat(320));

        UserRegistrationRequest request = new UserRegistrationRequest("testLogin", "tesPassword", sb.toString() + "@mail.ru");
        assertThrows(NotCorrectDataException.class, () -> authService.registrationUser(request));
    }

    @Test
    public void signIn_Positive() throws DataNotFoundException, WrongPasswordException {
        String login = "testLogin";
        String password = "testPassword";
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        UserSignInRequest request = new UserSignInRequest(login, password);

        User user = new User();
        user.setLogin(login);
        user.setPassword(encodedPassword);
        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        User resultUser = authService.signIn(request);

        assertEquals(resultUser, user);
    }

    @Test
    public void signIn_userNotFound_throwException() throws DataNotFoundException, WrongPasswordException {
        String login = "testLogin";
        String password = "testPassword";
        UserSignInRequest request = new UserSignInRequest(login, password);

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> authService.signIn(request));
    }

    @Test
    public void signIn_wrongPassword_throwException() throws DataNotFoundException, WrongPasswordException {
        String login = "testLogin";
        String password = "testPassword";
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        password = "test";
        UserSignInRequest request = new UserSignInRequest(login, password);

        User user = new User();
        user.setLogin(login);
        user.setPassword(encodedPassword);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
        assertThrows(WrongPasswordException.class, () -> authService.signIn(request));
    }

    @Test
    public void isValidEmail_Positive(){
        assertTrue(AuthServiceImpl.isValidEmail("testEmail@mail.ru"));
    }

    @Test
    public void isValidEmail_Negative_1(){
        assertFalse(AuthServiceImpl.isValidEmail("testEmailmail.ru"));
    }

    @Test
    public void isValidEmail_Negative_2(){
        assertFalse(AuthServiceImpl.isValidEmail("testEmail@mailru"));
    }

    @Test
    public void isValidEmail_Negative_3(){
        assertFalse(AuthServiceImpl.isValidEmail("testEmailru"));
    }

    @Test
    public void isValidEmail_Negative_4(){
        assertFalse(AuthServiceImpl.isValidEmail("testEmail!@##$$$%%^^^&&**()_+@mail.ru"));
    }
}
