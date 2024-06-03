package ftn.hotelsservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ftn.hotelsservice.AuthPostgresIntegrationTest;
import ftn.hotelsservice.domain.dtos.UserDto;
import ftn.hotelsservice.services.RestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Sql("/sql/lodgeAvailability.sql")
public class LodgeControllerTest extends AuthPostgresIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestService restService;

    @BeforeEach
    public void setup() {
        authenticateHost();
    }

    @Test
    public void testGetLodgeByIdSucessful() throws Exception {
        String lodgeId = "b86553e1-2552-41cb-9e40-7ef87c424850";

        mockMvc.perform(get("/api/lodge/" + lodgeId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(lodgeId))
                .andExpect(jsonPath("$.ownerId").value("e49fcab5-d45b-4556-9d91-14e58177fea6"))
                .andExpect(jsonPath("$.name").value("Vikendica"))
                .andExpect(jsonPath("$.location").value("Lokacija1"))
                .andExpect(jsonPath("$.minimalGuestNumber").value(1))
                .andExpect(jsonPath("$.maximalGuestNumber").value(3))
                .andExpect(jsonPath("$.approvalType").value("AUTOMATIC"));
    }

    @Test void testGetAllLodgesSucessful() throws Exception {
        String lodgeId1 = "b86553e1-2552-41cb-9e40-7ef87c424850";
        String lodgeId2 = "b86553e1-2552-41cb-9e40-7ef87c424852";

        String ownerId1 = "e49fcab5-d45b-4556-9d91-14e58177fea6";
        String ownerId2 = "e49fcab5-d45b-4556-9d91-14e58177fea1";

        mockMvc.perform(get("/api/lodge/all")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[0].id").value(lodgeId1))
                .andExpect(jsonPath("$[0].ownerId").value(ownerId1))
                .andExpect(jsonPath("$[0].name").value("Vikendica"))
                .andExpect(jsonPath("$[0].location").value("Lokacija1"))
                .andExpect(jsonPath("$[0].minimalGuestNumber").value(1))
                .andExpect(jsonPath("$[0].maximalGuestNumber").value(3))
                .andExpect(jsonPath("$[0].approvalType").value("AUTOMATIC"))

                .andExpect(jsonPath("$[1].id").value(lodgeId2))
                .andExpect(jsonPath("$[1].ownerId").value(ownerId2))
                .andExpect(jsonPath("$[1].name").value("Vikendica2"))
                .andExpect(jsonPath("$[1].location").value("Lokacija2"))
                .andExpect(jsonPath("$[1].minimalGuestNumber").value(1))
                .andExpect(jsonPath("$[1].maximalGuestNumber").value(3))
                .andExpect(jsonPath("$[1].approvalType").value("AUTOMATIC"));
    }

    @Test
    public void testGetAllMineLodgesSucessful() throws Exception {
        String ownerId = "e49fcab5-d45b-4556-9d91-14e58177fea6";
        String lodgeId = "b86553e1-2552-41cb-9e40-7ef87c424850";

        UserDto mockUserDTO = UserDto.builder()
                .id(UUID.fromString(ownerId))
                .build();

        when(restService.getUserById(any(UUID.class))).thenReturn(mockUserDTO);

        mockMvc.perform(get("/api/lodge/mine/all")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))

                .andExpect(jsonPath("$[0].id").value(lodgeId))
                .andExpect(jsonPath("$[0].ownerId").value(ownerId))
                .andExpect(jsonPath("$[0].name").value("Vikendica"))
                .andExpect(jsonPath("$[0].location").value("Lokacija1"))
                .andExpect(jsonPath("$[0].minimalGuestNumber").value(1))
                .andExpect(jsonPath("$[0].maximalGuestNumber").value(3))
                .andExpect(jsonPath("$[0].approvalType").value("AUTOMATIC"));

    }

}
