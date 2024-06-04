package ftn.hotelsservice.domain.dtos;

import ftn.hotelsservice.domain.converters.CsvConverter;
import ftn.hotelsservice.domain.entities.Photo;
import ftn.hotelsservice.domain.entities.RequestForReservationApprovalType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LodgeDto {

    private UUID id;
    private UUID ownerId;
    private String name;
    private String location;
    private List<String> amenities;
    private List<Photo> photos;
    private int minimalGuestNumber;
    private int maximalGuestNumber;
    private RequestForReservationApprovalType approvalType;

}
