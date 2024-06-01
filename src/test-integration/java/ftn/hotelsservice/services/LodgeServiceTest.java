package ftn.hotelsservice.services;

import ftn.hotelsservice.AuthPostgresIntegrationTest;
import ftn.hotelsservice.domain.dtos.LodgeCreateRequest;
import ftn.hotelsservice.domain.dtos.LodgeDto;
import ftn.hotelsservice.domain.dtos.UserDto;
import ftn.hotelsservice.domain.entities.RequestForReservationApprovalType;
import ftn.hotelsservice.exception.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LodgeServiceTest extends AuthPostgresIntegrationTest {

    @Autowired
    private LodgeService lodgeService;

    @MockBean
    private RestService restService;

    @BeforeEach
    public void setup() {
        authenticateHost();
    }

    @Test
    public void testCreateLodgeSucessful() {
        String userId = "e49fcab5-d45b-4556-9d91-14e58177fea6";

        LodgeCreateRequest lodgeCreateRequest = LodgeCreateRequest.builder()
                .name("Lodge1")
                .location("Location1")
                .amenities(Arrays.asList("wifi", "pool"))
                .minimalGuestNumber(1)
                .maximalGuestNumber(3)
                .approvalType(RequestForReservationApprovalType.AUTOMATIC)
                .build();

        UserDto mockUserDTO = UserDto.builder()
                .id(UUID.fromString(userId))
                .build();

        when(restService.getUserById(any(UUID.class))).thenReturn(mockUserDTO);

        LodgeDto createdLodge = lodgeService.create(lodgeCreateRequest, null);

        assertNotNull(createdLodge);
        assertEquals(lodgeCreateRequest.getName(), createdLodge.getName());
        assertEquals(lodgeCreateRequest.getLocation(), createdLodge.getLocation());
        assertEquals(lodgeCreateRequest.getAmenities(), createdLodge.getAmenities());
        assertEquals(lodgeCreateRequest.getMinimalGuestNumber(), createdLodge.getMinimalGuestNumber());
        assertEquals(lodgeCreateRequest.getMaximalGuestNumber(), createdLodge.getMaximalGuestNumber());
        assertEquals(lodgeCreateRequest.getApprovalType(), createdLodge.getApprovalType());

    }

    @Test
    public void testCreateLodgeUserDoesntExist() {
        String userId = "e49fcab5-d45b-4556-9d91-111111111111";

        LodgeCreateRequest lodgeCreateRequest = LodgeCreateRequest.builder()
                .name("Lodge1")
                .location("Location1")
                .amenities(Arrays.asList("wifi", "pool"))
                .minimalGuestNumber(1)
                .maximalGuestNumber(3)
                .approvalType(RequestForReservationApprovalType.AUTOMATIC)
                .build();

        when(restService.getUserById(any(UUID.class))).thenReturn(null);

        assertThrows(NotFoundException.class, () -> lodgeService.create(lodgeCreateRequest, null));

    }

}