package ftn.hotelsservice.services;

import ftn.hotelsservice.AuthPostgresIntegrationTest;
import ftn.hotelsservice.domain.dtos.LodgeAvailabilityPeriodCreateRequest;
import ftn.hotelsservice.domain.dtos.LodgeAvailabilityPeriodDto;
import ftn.hotelsservice.domain.dtos.UserDto;
import ftn.hotelsservice.domain.entities.PriceType;
import ftn.hotelsservice.exception.exceptions.BadRequestException;
import ftn.hotelsservice.exception.exceptions.NotFoundException;
import ftn.hotelsservice.repositories.LodgeAvailabilityRepository;
import ftn.hotelsservice.repositories.LodgeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Sql("/sql/lodgeAvailability.sql")
public class LodgeAvailabilityServiceTest extends AuthPostgresIntegrationTest {

    @MockBean
    private RestService restService;

    @Autowired
    private LodgeRepository lodgeRepository;

    @Autowired
    private LodgeAvailabilityRepository lodgeAvailabilityRepository;

    @Autowired
    private LodgeAvailabilityService lodgeAvailabilityService;

    @BeforeEach
    public void setup() {
        authenticateHost();
    }

    @Test
    public void testCreateLodgeAvailabilityPeriodSucessful() {
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

        LodgeAvailabilityPeriodDto response = lodgeAvailabilityService.create(availabilityCreateRequest);

        assertNotNull(response);
        assertEquals(availabilityCreateRequest.getLodgeId(), response.getLodgeId());
        assertEquals(availabilityCreateRequest.getDateFrom(), response.getDateFrom());
        assertEquals(availabilityCreateRequest.getDateTo(), response.getDateTo());
        assertEquals(availabilityCreateRequest.getPriceType(), response.getPriceType());
        assertEquals(availabilityCreateRequest.getPrice(), response.getPrice());

    }

    @Test
    public void testCreateLodgeAvailabilityPeriodLodgeDoesntExist() {
        String userId = "e49fcab5-d45b-4556-9d91-14e58177fea6";
        LodgeAvailabilityPeriodCreateRequest availabilityCreateRequest = LodgeAvailabilityPeriodCreateRequest.builder()
                .lodgeId(UUID.fromString("b86553e1-2552-41cb-9e40-7ef87c424851"))
                .dateFrom(LocalDateTime.parse("2024-06-01 20:10:21.2632212", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS")))
                .dateTo(LocalDateTime.parse("2024-06-03 20:10:21.2632212", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS")))
                .priceType(PriceType.PER_LODGE)
                .price(20.0)
                .build();

        UserDto mockUserDTO = UserDto.builder()
                .id(UUID.fromString(userId))
                .build();

        when(restService.getUserById(any(UUID.class))).thenReturn(mockUserDTO);

        assertThrows(NotFoundException.class, () -> lodgeAvailabilityService.create(availabilityCreateRequest));

    }

    @Test
    public void testCreateLodgeAvailabilityPeriodUserIsNotLodgeOwner() {
        String userId = "e49fcab5-d45b-4556-9d91-14e58177fea6";
        LodgeAvailabilityPeriodCreateRequest availabilityCreateRequest = LodgeAvailabilityPeriodCreateRequest.builder()
                .lodgeId(UUID.fromString("b86553e1-2552-41cb-9e40-7ef87c424852"))
                .dateFrom(LocalDateTime.parse("2024-06-01 20:10:21.2632212", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS")))
                .dateTo(LocalDateTime.parse("2024-06-03 20:10:21.2632212", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS")))
                .priceType(PriceType.PER_LODGE)
                .price(20.0)
                .build();

        UserDto mockUserDTO = UserDto.builder()
                .id(UUID.fromString(userId))
                .build();

        when(restService.getUserById(any(UUID.class))).thenReturn(mockUserDTO);

        assertThrows(BadRequestException.class, () -> lodgeAvailabilityService.create(availabilityCreateRequest));

    }

    @Test
    public void testCreateLodgeAvailabilityPeriodDateFromIsNotBeforeDateTo() {
        String userId = "e49fcab5-d45b-4556-9d91-14e58177fea6";
        LodgeAvailabilityPeriodCreateRequest availabilityCreateRequest = LodgeAvailabilityPeriodCreateRequest.builder()
                .lodgeId(UUID.fromString("b86553e1-2552-41cb-9e40-7ef87c424850"))
                .dateFrom(LocalDateTime.parse("2024-06-03 20:10:21.2632212", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS")))
                .dateTo(LocalDateTime.parse("2024-06-01 20:10:21.2632212", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS")))
                .priceType(PriceType.PER_LODGE)
                .price(20.0)
                .build();

        UserDto mockUserDTO = UserDto.builder()
                .id(UUID.fromString(userId))
                .build();

        when(restService.getUserById(any(UUID.class))).thenReturn(mockUserDTO);

        assertThrows(BadRequestException.class, () -> lodgeAvailabilityService.create(availabilityCreateRequest));

    }

    @Test
    public void testCreateLodgeAvailabilityPeriodDateFromIsNotOneDayApartFromDateTo() {
        String userId = "e49fcab5-d45b-4556-9d91-14e58177fea6";
        LodgeAvailabilityPeriodCreateRequest availabilityCreateRequest = LodgeAvailabilityPeriodCreateRequest.builder()
                .lodgeId(UUID.fromString("b86553e1-2552-41cb-9e40-7ef87c424850"))
                .dateFrom(LocalDateTime.parse("2024-06-01 20:10:21.2632212", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS")))
                .dateTo(LocalDateTime.parse("2024-06-02 20:10:21.2632212", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS")))
                .priceType(PriceType.PER_LODGE)
                .price(20.0)
                .build();

        UserDto mockUserDTO = UserDto.builder()
                .id(UUID.fromString(userId))
                .build();

        when(restService.getUserById(any(UUID.class))).thenReturn(mockUserDTO);

        assertThrows(BadRequestException.class, () -> lodgeAvailabilityService.create(availabilityCreateRequest));

    }

    @Test
    public void testCreateLodgeAvailabilityPeriodOverlappingPeriodExists() {
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

        LodgeAvailabilityPeriodDto response = lodgeAvailabilityService.create(availabilityCreateRequest);

        assertNotNull(response);
        assertEquals(availabilityCreateRequest.getLodgeId(), response.getLodgeId());
        assertEquals(availabilityCreateRequest.getDateFrom(), response.getDateFrom());
        assertEquals(availabilityCreateRequest.getDateTo(), response.getDateTo());
        assertEquals(availabilityCreateRequest.getPriceType(), response.getPriceType());
        assertEquals(availabilityCreateRequest.getPrice(), response.getPrice());

        LodgeAvailabilityPeriodCreateRequest availabilityCreateRequest2 = LodgeAvailabilityPeriodCreateRequest.builder()
                .lodgeId(UUID.fromString("b86553e1-2552-41cb-9e40-7ef87c424850"))
                .dateFrom(LocalDateTime.parse("2024-05-01 20:10:21.2632212", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS")))
                .dateTo(LocalDateTime.parse("2024-07-03 20:10:21.2632212", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSS")))
                .priceType(PriceType.PER_LODGE)
                .price(20.0)
                .build();

        assertThrows(BadRequestException.class, () -> lodgeAvailabilityService.create(availabilityCreateRequest2));

    }

}
