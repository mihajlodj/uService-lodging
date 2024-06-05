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

import java.util.*;

@Entity
@Table(name = "lodge")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lodge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID ownerId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = false)
    private String location;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = CsvConverter.class)
    private List<String> amenities;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "lodge_id")
    private List<Photo> photos;

    @Column(nullable = false)
    private int minimalGuestNumber;

    @Column(nullable = false)
    private int maximalGuestNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RequestForReservationApprovalType approvalType = RequestForReservationApprovalType.AUTOMATIC;

    @OneToMany(mappedBy = "lodge", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LodgeAvailabilityPeriod> availabilityPeriods = new ArrayList<>();

}
