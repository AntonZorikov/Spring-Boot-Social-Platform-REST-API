package com.example.jeddit;

import com.example.jeddit.controllers.AuthController;
import com.example.jeddit.exceptions.DataNotFoundException;
import com.example.jeddit.exceptions.NotCorrectDataException;
import com.example.jeddit.exceptions.NotUniqueDataException;
import com.example.jeddit.exceptions.WrongPasswordException;
import com.example.jeddit.models.entitys.User;
import com.example.jeddit.models.models.auth.UserRegistrationRequest;
import com.example.jeddit.models.models.auth.UserSignInRequest;
import com.example.jeddit.servicies.AuthService;
import com.example.jeddit.servicies.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testRegisterOk() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest();
        User user = new User();

        Mockito.when(authService.registrationUser(any(UserRegistrationRequest.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testRegister_NotCorrectDataException() throws Exception {
        testRegister(400, new NotCorrectDataException());
    }

    @Test
    public void testRegister_NotUniqueDataException() throws Exception {
        testRegister(409, new NotUniqueDataException());
    }

    private void testRegister(int expectedStatus, Exception exception) throws Exception {
        Mockito.when(authService.registrationUser(any(UserRegistrationRequest.class))).thenThrow(exception);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .content(asJsonString(new UserRegistrationRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    public void testSigninOk() throws Exception {
        UserSignInRequest request = new UserSignInRequest();
        User user = new User();

        Mockito.when(authService.signIn(any(UserSignInRequest.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testSignin_DataNotFoundException() throws Exception {
        testSignin(401, new DataNotFoundException());
    }

    @Test
    public void testSignin_WrongPasswordException() throws Exception {
        testSignin(401, new WrongPasswordException());
    }

    private void testSignin(int expectedStatus, Exception exception) throws Exception {
        Mockito.when(authService.signIn(any(UserSignInRequest.class))).thenThrow(exception);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                        .content(asJsonString(new UserSignInRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedStatus));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
