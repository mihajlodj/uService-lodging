package ftn.hotelsservice.services;

import ftn.hotelsservice.domain.dtos.*;
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
import org.springframework.web.bind.annotation.RequestBody;

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
        checkIfLoggedInUserIsLodgeOwner(lodge);
        checkDatesRangeIsValid(lodgeAvailabilityPeriod.getDateFrom(), lodgeAvailabilityPeriod.getDateTo());
        checkForOverlappingAvailabilityPeriods(lodgeAvailabilityPeriod, lodge);
        return LodgeAvailabilityPeriodMapper.INSTANCE.toDto(lodgeAvailabilityRepository.save(lodgeAvailabilityPeriod));
    }

    public LodgeAvailabilityPeriodDto update(UUID lodgeAvailabilityPeriodId, LodgeAvailabilityPeriodUpdateRequest updateRequest) {
        LodgeAvailabilityPeriod lodgeAvailabilityPeriod = getLodgeAvailabilityPeriod(lodgeAvailabilityPeriodId);
        Lodge lodge = getLodge(lodgeAvailabilityPeriod.getLodge().getId());
        checkIfLoggedInUserIsLodgeOwner(lodge);
        LodgeAvailabilityPeriodMapper.INSTANCE.update(lodgeAvailabilityPeriod, updateRequest);
        checkDatesRangeIsValid(lodgeAvailabilityPeriod.getDateFrom(), lodgeAvailabilityPeriod.getDateTo());
        checkForOverlappingAvailabilityPeriods(lodgeAvailabilityPeriod, lodge);
        return LodgeAvailabilityPeriodMapper.INSTANCE.toDto(lodgeAvailabilityRepository.save(lodgeAvailabilityPeriod));
    }

    private void checkIfLoggedInUserIsLodgeOwner(Lodge lodge) {
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

    private LodgeAvailabilityPeriod getLodgeAvailabilityPeriod(UUID lodgeAvailabilityPeriodId) {
        return lodgeAvailabilityRepository.findById(lodgeAvailabilityPeriodId).orElseThrow(() -> new NotFoundException("LodgeAvailabilityPeriod doesn't exist"));
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
            if (lodgeAvailabilityPeriod.getId() != null && lodgeAvailabilityPeriod.getId().equals(existingLodgeAvailabilityPeriod.getId())) {
                continue;       // if lodgeAvailabilityPeriod exists do not execute check
            }
            if (existingLodgeAvailabilityPeriod.getDateTo().isAfter(lodgeAvailabilityPeriod.getDateFrom())
                    && existingLodgeAvailabilityPeriod.getDateFrom().isBefore(lodgeAvailabilityPeriod.getDateTo())) {
                throw new BadRequestException("There is overlapping availability period with this one.");
            }
        }
    }

    public List<LodgeAvailabilityPeriod> getAvailabilityPeriodsForLodge(Lodge lodge) {
        return lodgeAvailabilityRepository.findByLodgeId(lodge.getId());
    }

    public void delete(UUID id) {
        LodgeAvailabilityPeriod lodgeAvailabilityPeriod = lodgeAvailabilityRepository.findById(id).orElseThrow(() -> new NotFoundException("LodgeAvailabilityPeriod doesn't exist"));
        checkForDeletionIfLodgedInUserIsOwner(lodgeAvailabilityPeriod);
        //TODO check if there are no reservations in this LodgeAvailabilityPeriod, if there are no delete entity, else don't delete
        lodgeAvailabilityRepository.deleteById(id);
    }

    private void checkForDeletionIfLodgedInUserIsOwner(LodgeAvailabilityPeriod lodgeAvailabilityPeriod) {
        Lodge lodge = lodgeAvailabilityPeriod.getLodge();
        UserDto owner = getLoggedInUser();
        if (!lodge.getOwnerId().equals(owner.getId())) {
            throw new BadRequestException("You can't delete LodgeAvailabilityPeriod for Lodges you don't own.");
        }
    }

    public List<LodgeAvailabilityPeriodDto> getAllAvailabilityPeriodsForLodge(UUID lodgeId) {
        Lodge lodge = getLodge(lodgeId);
        List<LodgeAvailabilityPeriod> availabilityPeriods = lodgeAvailabilityRepository.findByLodgeId(lodge.getId());

        return LodgeAvailabilityPeriodMapper.INSTANCE.toDto(availabilityPeriods);
    }

    public List<LodgeAvailabilityPeriodInterserviceDto> getAllAvailabilityPeriodsForLodgeInterservice(UUID lodgeId) {
        Lodge lodge = getLodge(lodgeId);
        List<LodgeAvailabilityPeriod> availabilityPeriods = lodgeAvailabilityRepository.findByLodgeId(lodge.getId());

        return LodgeAvailabilityPeriodMapper.INSTANCE.toInterserviceDto(availabilityPeriods);
    }

}
