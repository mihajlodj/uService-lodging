package ftn.hotelsservice.controllers;

import ftn.hotelsservice.domain.dtos.LodgeAvailabilityPeriodCreateRequest;
import ftn.hotelsservice.domain.dtos.LodgeAvailabilityPeriodUpdateRequest;
import ftn.hotelsservice.domain.dtos.LodgeCreateRequest;
import ftn.hotelsservice.services.LodgeAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lodge/availability")
@RequiredArgsConstructor
public class LodgeAvailabilityController {

    private final LodgeAvailabilityService lodgeAvailabilityService;

    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<?> createLodgeAvailabilityPeriod(@RequestBody @Valid LodgeAvailabilityPeriodCreateRequest request) {
        return ResponseEntity.ok(lodgeAvailabilityService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<?> updateLodge(@PathVariable UUID id, @RequestBody LodgeAvailabilityPeriodUpdateRequest updateRequest) {
        return ResponseEntity.ok(lodgeAvailabilityService.update(id, updateRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        lodgeAvailabilityService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<?> getAllAvailabilityPeriodsForLodge(@PathVariable UUID id) {
        return ResponseEntity.ok(lodgeAvailabilityService.getAllAvailabilityPeriodsForLodge(id));
    }

    @GetMapping("/all/interservice/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllAvailabilityPeriodsForLodgeInterservice(@PathVariable UUID id) {
        return ResponseEntity.ok(lodgeAvailabilityService.getAllAvailabilityPeriodsForLodgeInterservice(id));
    }

}
