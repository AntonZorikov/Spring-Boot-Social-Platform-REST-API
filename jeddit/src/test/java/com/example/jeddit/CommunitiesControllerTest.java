package com.example.jeddit;

import com.example.jeddit.controllers.CommunitiesController;
import com.example.jeddit.exceptions.*;
import com.example.jeddit.models.models.JWTTokenRequest;
import com.example.jeddit.models.models.communities.CommunitiesCreateRequest;
import com.example.jeddit.models.models.communities.CommunityChangeDescriptionRequest;
import com.example.jeddit.models.models.communities.CommunityInfoResponse;
import com.example.jeddit.servicies.CommunitiesService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CommunitiesControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CommunitiesService communitiesService;

    @InjectMocks
    private CommunitiesController communitiesController;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(communitiesController).build();
    }

    @Test
    public void testCreateCommunityOk() throws Exception {
        CommunitiesCreateRequest request = new CommunitiesCreateRequest();
        Mockito.doNothing().when(communitiesService).create(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/communities/")
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateCommunity_NotUniqueDataException() throws Exception {
        testCreateCommunity(409, 409, new NotUniqueDataException());
    }

    @Test
    public void testCreateCommunity_NotValidToken() throws Exception {
        testCreateCommunity(401, 401, new NotValidToken());
    }

    @Test
    public void testCreateCommunity_NotCorrectDataException() throws Exception {
        testCreateCommunity(400, 400, new NotCorrectDataException());
    }

    private void testCreateCommunity(int expectedStatus, int expectedErrorCode, Exception exception) throws Exception {
        Mockito.doThrow(exception).when(communitiesService).create(any(CommunitiesCreateRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/communities/")
                        .content(asJsonString(new CommunitiesCreateRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedStatus))
                .andExpect(jsonPath("$.error.code").value(expectedErrorCode));
    }

    @Test
    public void testGetCommunityOk() throws Exception {
        String communityTitle = "sample_community";
        CommunityInfoResponse response = new CommunityInfoResponse();
        Mockito.when(communitiesService.get(anyString())).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/communities/{title}", communityTitle).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCommunity_DataNotFoundException() throws Exception {
        testGetCommunity(400, new DataNotFoundException());
    }

    private void testGetCommunity(int expectedStatus, Exception exception) throws Exception {
        Mockito.doThrow(exception).when(communitiesService).get(anyString());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/communities/{title}", "sample_community").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    public void testChangeDescriptionOk() throws Exception {
        String communityTitle = "sample_community";
        CommunityChangeDescriptionRequest request = new CommunityChangeDescriptionRequest();
        Mockito.doNothing().when(communitiesService).changeDescription(anyString(), any(CommunityChangeDescriptionRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/communities/{title}/change_description", communityTitle)
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testChangeDescription_DataNotFoundException() throws Exception {
        testChangeDescription(400, new DataNotFoundException());
    }

    @Test
    public void testChangeDescription_NotCorrectDataException() throws Exception {
        testChangeDescription(400, new NotCorrectDataException());
    }

    @Test
    public void testChangeDescription_NotEnoughRightsException() throws Exception {
        testChangeDescription(401, new NotEnoughRightsException());
    }

    @Test
    public void testChangeDescription_NotValidToken() throws Exception {
        testChangeDescription(401, new NotValidToken());
    }

    private void testChangeDescription(int expectedStatus, Exception exception) throws Exception {
        Mockito.doThrow(exception).when(communitiesService).changeDescription(anyString(), any(CommunityChangeDescriptionRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/communities/{title}/change_description", "sample_community")
                        .content(asJsonString(new CommunityChangeDescriptionRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedStatus));
    }

    @Test
    public void testDeleteCommunityOk() throws Exception {
        String communityTitle = "sample_community";
        JWTTokenRequest request = new JWTTokenRequest();
        Mockito.doNothing().when(communitiesService).delete(anyString(), any(JWTTokenRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/communities/{title}", communityTitle)
                        .content(asJsonString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteCommunity_DataNotFoundException() throws Exception {
        testDeleteCommunity(400, new DataNotFoundException());
    }

    @Test
    public void testDeleteCommunity_NotEnoughRightsException() throws Exception {
        testDeleteCommunity(401, new NotEnoughRightsException());
    }

    @Test
    public void testDeleteCommunity_NotValidToken() throws Exception {
        testDeleteCommunity(401, new NotValidToken());
    }

    private void testDeleteCommunity(int expectedStatus, Exception exception) throws Exception {
        Mockito.doThrow(exception).when(communitiesService).delete(anyString(), any(JWTTokenRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/communities/{title}", "sample_community")
                        .content(asJsonString(new JWTTokenRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedStatus));
    }
}
