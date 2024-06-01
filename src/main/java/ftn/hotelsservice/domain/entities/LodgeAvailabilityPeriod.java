package ftn.hotelsservice.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ftn.hotelsservice.domain.converters.CsvConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "\"lodge_availability_period\"")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LodgeAvailabilityPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "lodge_id", nullable = false)
    private Lodge lodge;

    @Column(nullable = false)
    private LocalDateTime dateFrom;

    @Column(nullable = false)
    private LocalDateTime dateTo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PriceType priceType = PriceType.PER_LODGE;

    @Column(nullable = false)
    private double price;
}
