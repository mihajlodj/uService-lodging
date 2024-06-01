package ftn.hotelsservice.controllers;

import ftn.hotelsservice.domain.dtos.LodgeCreateRequest;
import ftn.hotelsservice.domain.dtos.LodgeDto;
import ftn.hotelsservice.domain.entities.Lodge;
import ftn.hotelsservice.domain.mappers.LodgeMapper;
import ftn.hotelsservice.services.LodgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/lodge")
@RequiredArgsConstructor
public class LodgeController {

     private final LodgeService lodgeService;

    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<?> createLodge(@RequestPart("lodgeCreateRequest") @Valid LodgeCreateRequest lodgeCreateRequest,
                                @RequestPart("photos") List<MultipartFile> photos) {
        return ResponseEntity.ok(lodgeService.create(lodgeCreateRequest, photos));
    }

}
