package ftn.hotelsservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ftn.hotelsservice.AuthPostgresIntegrationTest;
import ftn.hotelsservice.domain.dtos.LodgeAvailabilityPeriodCreateRequest;
import ftn.hotelsservice.domain.dtos.LodgeAvailabilityPeriodDto;
import ftn.hotelsservice.domain.dtos.LodgeAvailabilityPeriodUpdateRequest;
import ftn.hotelsservice.domain.dtos.UserDto;
import ftn.hotelsservice.domain.entities.PriceType;
import ftn.hotelsservice.services.RestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Sql("/sql/lodgeAvailability.sql")
public class LodgeAvailabilityControllerTest extends AuthPostgresIntegrationTest {

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
    public void testCreateLodgeAvailabilityPeriodSucessful() throws Exception {
        String userId = "e49fcab5-d45b-4556-9d91-14e58177fea6";
        LodgeAvailabilityPeriodCreateRequest availabilityCreateRequest = LodgeAvailabilityPeriodCreateRequest.builder()
                .lodgeId(UUID.fromString("b86553e1-2552-41cb-9e40-7ef87c424850"))
                .dateFrom(LocalDateTime.parse("2024-06-01 20:10:21.2632212", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS")))
                .dateTo(LocalDateTime.parse("2024-06-03 20:10:21.2632212", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS")))
                .priceType(PriceType.PER_LODGE)
                .price(20.0)
                .build();

        UserDto mockUserDTO = UserDto.builder()
                .id(UUID.fromString(userId))
                .build();

        when(restService.getUserById(any(UUID.class))).thenReturn(mockUserDTO);

        mockMvc.perform(post("/api/lodge/availability/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(availabilityCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lodgeId").value("b86553e1-2552-41cb-9e40-7ef87c424850"))
                .andExpect(jsonPath("$.dateFrom").value("2024-06-01 20:10:21.2632212"))
                .andExpect(jsonPath("$.dateTo").value("2024-06-03 20:10:21.2632212"))
                .andExpect(jsonPath("$.priceType").value("PER_LODGE"))
                .andExpect(jsonPath("$.price").value(20.0));

    }

    @Test
    public void testUpdateLodgeAvailabilityPeriodSucessful() throws Exception {
        String userId = "e49fcab5-d45b-4556-9d91-14e58177fea6";
        String lodgeAvailabilityPeriodId = "fb809d54-332d-4811-8d93-d3ddf2f345a2";
        LodgeAvailabilityPeriodUpdateRequest request = LodgeAvailabilityPeriodUpdateRequest.builder()
                .dateFrom(LocalDateTime.parse("2024-06-01 20:10:21.2632212", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS")))
                .dateTo(LocalDateTime.parse("2024-06-03 20:10:21.2632212", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS")))
                .priceType(PriceType.PER_LODGE)
                .price(20.0)
                .build();

        UserDto mockUserDTO = UserDto.builder()
                .id(UUID.fromString(userId))
                .build();

        when(restService.getUserById(any(UUID.class))).thenReturn(mockUserDTO);

        mockMvc.perform(put("/api/lodge/availability/" + lodgeAvailabilityPeriodId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lodgeId").value("b86553e1-2552-41cb-9e40-7ef87c424850"))
                .andExpect(jsonPath("$.dateFrom").value("2024-06-01 20:10:21.2632212"))
                .andExpect(jsonPath("$.dateTo").value("2024-06-03 20:10:21.2632212"))
                .andExpect(jsonPath("$.priceType").value("PER_LODGE"))
                .andExpect(jsonPath("$.price").value(20.0));

    }

}
