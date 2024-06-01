package ftn.hotelsservice.domain.dtos;

import ftn.hotelsservice.domain.entities.RequestForReservationApprovalType;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LodgeCreateRequest {

    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Location is required")
    private String location;

    @NotEmpty(message = "Amenities are required")
    private List<String> amenities;

    @Min(value = 1, message = "Minimal guest number must be at least 1")
    private int minimalGuestNumber;

    @Min(value = 1, message = "Maximal guest number must be at least 1")
    private int maximalGuestNumber;

    @NotNull(message = "Approval type is required")
    @Builder.Default
    private RequestForReservationApprovalType approvalType = RequestForReservationApprovalType.AUTOMATIC;
}
