package ftn.hotelsservice.controllers;

import ftn.hotelsservice.domain.dtos.LodgeAvailabilityPeriodCreateRequest;
import ftn.hotelsservice.domain.dtos.LodgeCreateRequest;
import ftn.hotelsservice.services.LodgeAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

}
