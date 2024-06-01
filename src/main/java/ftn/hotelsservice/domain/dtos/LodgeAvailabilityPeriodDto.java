package ftn.hotelsservice.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import ftn.hotelsservice.domain.entities.Lodge;
import ftn.hotelsservice.domain.entities.PriceType;
import jakarta.persistence.*;
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
public class LodgeAvailabilityPeriodDto {

    private UUID id;
    private UUID lodgeId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSSS")
    private LocalDateTime dateFrom;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSSS")
    private LocalDateTime dateTo;

    private PriceType priceType;
    private double price;

}
