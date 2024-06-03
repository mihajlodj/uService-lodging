package ftn.hotelsservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ftn.hotelsservice.AuthPostgresIntegrationTest;
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

}
