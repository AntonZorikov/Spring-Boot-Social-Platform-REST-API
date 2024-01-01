package com.example.jeddit;

import com.example.jeddit.controllers.UsersController;
import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.models.users.UserChangePasswordRequest;
import com.example.jeddit.servicies.UsersService;
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
public class UsersControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UsersService usersService;

    @InjectMocks
    private UsersController usersController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
    }

    private void testChangePassword(int expectedStatus, int expectedErrorCode, Exception exception) throws Exception {
        Mockito.doThrow(exception).when(usersService).changePassword(anyLong(), any(UserChangePasswordRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}/change_password", 1)
                        .content(asJsonString(new UserChangePasswordRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedStatus))
                .andExpect(jsonPath("$.error.code").value(expectedErrorCode));
    }

    @Test
    public void testChangePasswordOk() throws Exception {
        long userId = 1L;
        UserChangePasswordRequest request = new UserChangePasswordRequest();
        Mockito.doNothing().when(usersService).changePassword(anyLong(), any(UserChangePasswordRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}/change_password", userId)
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testChangePassword_WrongPasswordException() throws Exception {
        testChangePassword(401, 401, new WrongPasswordException());
    }

    @Test
    public void testChangePassword_NotValidToken() throws Exception {
        testChangePassword(401, 401, new NotValidToken());
    }

    @Test
    public void testChangePassword_DataNotFoundException() throws Exception {
        testChangePassword(400, 400, new DataNotFoundException());
    }

    @Test
    public void testChangePassword_NotCorrectDataException() throws Exception {
        testChangePassword(400, 400, new DataNotFoundException());
    }

    @Test
    public void testChangePassword_NotEnoughRightsException() throws Exception {
        testChangePassword(403, 403, new NotEnoughRightsException());
    }



    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
