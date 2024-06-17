package ftn.hotelsservice.controllers;

import ftn.hotelsservice.domain.dtos.LodgeCreateRequest;
import ftn.hotelsservice.domain.dtos.LodgeDto;
import ftn.hotelsservice.domain.dtos.LodgeSearchRequest;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/lodge")
@RequiredArgsConstructor
public class LodgeController {

     private final LodgeService lodgeService;

    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<?> createLodge(@RequestPart("lodgeCreateRequest") @Valid LodgeCreateRequest lodgeCreateRequest,
                                @RequestPart(value="photos", required = false) List<MultipartFile> photos) {
        return ResponseEntity.ok(lodgeService.create(lodgeCreateRequest, photos));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getLodgeById(@PathVariable UUID id) {
        return ResponseEntity.ok(lodgeService.getLodgeById(id));
    }

    @GetMapping(value = "/interservice/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getLodgeByIdInterservice(@PathVariable UUID id) {
        return ResponseEntity.ok(lodgeService.getLodgeByIdInterservice(id));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllLodges() {
        return ResponseEntity.ok(lodgeService.getAllLodges());
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> searchLodges(LodgeSearchRequest searchRequest) {
        return ResponseEntity.ok(lodgeService.searchLodges(searchRequest));
    }

    @GetMapping(value = "/mine/all")
    @PreAuthorize("hasAuthority('HOST')")
    public ResponseEntity<?> getMyLodges() {
        return ResponseEntity.ok(lodgeService.getAllMineLodges());
    }

}
