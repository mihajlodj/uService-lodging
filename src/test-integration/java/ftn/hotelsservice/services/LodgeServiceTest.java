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

@Sql("/sql/lodgeAvailability.sql")
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

    @Test
    public void testGetLodgeByIdSucessful() {
        UUID lodgeId = UUID.fromString("b86553e1-2552-41cb-9e40-7ef87c424850");

        LodgeDto retrievedDto = lodgeService.getLodgeById(lodgeId);
        assertEquals(lodgeId, retrievedDto.getId());
        assertEquals("Vikendica", retrievedDto.getName());
        assertEquals("Lokacija1", retrievedDto.getLocation());
        assertEquals(Arrays.asList("wifi", "bazen"), retrievedDto.getAmenities());
        assertEquals(1, retrievedDto.getMinimalGuestNumber());
        assertEquals(3, retrievedDto.getMaximalGuestNumber());
        assertEquals(RequestForReservationApprovalType.AUTOMATIC, retrievedDto.getApprovalType());

    }

    @Test
    public void testGetLodgeByIdLodgeDoesntExist() {
        UUID lodgeId = UUID.fromString("b86553e1-2552-41cb-9e40-7ef87c42485a");

        assertThrows(NotFoundException.class, () -> lodgeService.getLodgeById(lodgeId));
    }

    @Test
    public void testGetAllLodgesSucessful() {
        List<LodgeDto> retrievedDto = lodgeService.getAllLodges();

        assertEquals(2, retrievedDto.size());

        LodgeDto lodge1 = retrievedDto.get(0);
        assertEquals(UUID.fromString("b86553e1-2552-41cb-9e40-7ef87c424850"), lodge1.getId());
        assertEquals("Vikendica", lodge1.getName());
        assertEquals("Lokacija1", lodge1.getLocation());
        assertEquals(Arrays.asList("wifi", "bazen"), lodge1.getAmenities());
        assertEquals(1, lodge1.getMinimalGuestNumber());
        assertEquals(3, lodge1.getMaximalGuestNumber());
        assertEquals(RequestForReservationApprovalType.AUTOMATIC, lodge1.getApprovalType());

        LodgeDto lodge2 = retrievedDto.get(1);
        assertEquals(UUID.fromString("b86553e1-2552-41cb-9e40-7ef87c424852"), lodge2.getId());
        assertEquals("Vikendica2", lodge2.getName());
        assertEquals("Lokacija2", lodge2.getLocation());
        assertEquals(Arrays.asList("wifi", "bazen"), lodge2.getAmenities());
        assertEquals(1, lodge2.getMinimalGuestNumber());
        assertEquals(3, lodge2.getMaximalGuestNumber());
        assertEquals(RequestForReservationApprovalType.AUTOMATIC, lodge2.getApprovalType());

    }

}
