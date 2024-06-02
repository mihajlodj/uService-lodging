package ftn.hotelsservice.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import ftn.hotelsservice.domain.entities.Lodge;
import ftn.hotelsservice.domain.entities.PriceType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LodgeAvailabilityPeriodUpdateRequest {

    @NotNull(message = "DateFrom can't be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSSS")
    private LocalDateTime dateFrom;

    @NotNull(message = "DateTo can't be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSSS")
    private LocalDateTime dateTo;

    @NotNull(message = "Approval type is required")
    @Builder.Default
    private PriceType priceType = PriceType.PER_LODGE;

    @Min(value = 1, message = "Minimal price must be at least 1")
    private double price;

}
