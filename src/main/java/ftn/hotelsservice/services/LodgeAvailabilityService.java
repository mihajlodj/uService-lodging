package ftn.hotelsservice.services;

import ftn.hotelsservice.domain.dtos.LodgeAvailabilityPeriodCreateRequest;
import ftn.hotelsservice.domain.dtos.LodgeAvailabilityPeriodDto;
import ftn.hotelsservice.domain.dtos.UserDto;
import ftn.hotelsservice.domain.entities.Lodge;
import ftn.hotelsservice.domain.entities.LodgeAvailabilityPeriod;
import ftn.hotelsservice.domain.mappers.LodgeAvailabilityPeriodMapper;
import ftn.hotelsservice.exception.exceptions.BadRequestException;
import ftn.hotelsservice.exception.exceptions.NotFoundException;
import ftn.hotelsservice.repositories.LodgeAvailabilityRepository;
import ftn.hotelsservice.repositories.LodgeRepository;
import ftn.hotelsservice.utils.AuthUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class LodgeAvailabilityService {

    private final RestService restService;

    private final LodgeRepository lodgeRepository;

    private final LodgeAvailabilityRepository lodgeAvailabilityRepository;

    public LodgeAvailabilityPeriodDto create(LodgeAvailabilityPeriodCreateRequest availabilityCreateRequest) {
        LodgeAvailabilityPeriod lodgeAvailabilityPeriod = LodgeAvailabilityPeriodMapper.INSTANCE.fromCreateRequest(availabilityCreateRequest);
        Lodge lodge = getLodge(availabilityCreateRequest.getLodgeId());
        lodgeAvailabilityPeriod.setLodge(lodge);
        checkIfLoggedInUserIsLodgeOwner(availabilityCreateRequest, lodge);
        checkDatesRangeIsValid(lodgeAvailabilityPeriod.getDateFrom(), lodgeAvailabilityPeriod.getDateTo());
        checkForOverlappingAvailabilityPeriods(lodgeAvailabilityPeriod, lodge);
        return LodgeAvailabilityPeriodMapper.INSTANCE.toDto(lodgeAvailabilityRepository.save(lodgeAvailabilityPeriod));
    }

    private void checkIfLoggedInUserIsLodgeOwner(LodgeAvailabilityPeriodCreateRequest availabilityCreateRequest, Lodge lodge) {
        UserDto owner = getLoggedInUser();
        if (!lodge.getOwnerId().equals(owner.getId())) {
            throw new BadRequestException("You can create LodgeAvailabilityPeriod only for Lodges you own.");
        }
    }

    private UserDto getLoggedInUser() {
        UUID id = AuthUtils.getLoggedUserId();
        UserDto user = restService.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User doesn't exist");
        }
        return user;
    }

    private Lodge getLodge(UUID lodgeId) {
        return lodgeRepository.findById(lodgeId).orElseThrow(() -> new NotFoundException("Lodge doesn't exist"));
    }

    public void checkDatesRangeIsValid(LocalDateTime dateFrom, LocalDateTime dateTo) {
        // Check if dateFrom is earlier than dateTo
        if (!dateFrom.isBefore(dateTo)) {
            throw new BadRequestException("DateFrom is not earlier than DateTo.");
        }
        // Check if there is at least one day between dateFrom and dateTo
        if (!dateFrom.plusDays(1).isBefore(dateTo)) {
            throw new BadRequestException("Between DateFrom and DateTo needs to be at least one day.");
        }
    }

    public void checkForOverlappingAvailabilityPeriods(LodgeAvailabilityPeriod lodgeAvailabilityPeriod, Lodge lodge) {
        List<LodgeAvailabilityPeriod> lodgeAvailabilityPeriods = getAvailabilityPeriodsForLodge(lodge);
        for (LodgeAvailabilityPeriod existingLodgeAvailabilityPeriod : lodgeAvailabilityPeriods) {
            if (existingLodgeAvailabilityPeriod.getDateTo().isAfter(lodgeAvailabilityPeriod.getDateFrom())
                    && existingLodgeAvailabilityPeriod.getDateFrom().isBefore(lodgeAvailabilityPeriod.getDateTo())) {
                throw new BadRequestException("There is overlapping availability period with this one.");
            }
        }
    }

    public List<LodgeAvailabilityPeriod> getAvailabilityPeriodsForLodge(Lodge lodge) {
        return lodgeAvailabilityRepository.findByLodgeId(lodge.getId());
    }

}
