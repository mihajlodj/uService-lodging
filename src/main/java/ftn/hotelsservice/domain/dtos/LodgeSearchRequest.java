package ftn.hotelsservice.domain.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import ftn.hotelsservice.domain.entities.QLodge;
import ftn.hotelsservice.domain.entities.QLodgeAvailabilityPeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LodgeSearchRequest {

    private String location;
    private Integer guestNumber;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;

    @JsonIgnore
    @Builder.Default
    QLodge qLodge = QLodge.lodge;

    @JsonIgnore
    @Builder.Default
    QLodgeAvailabilityPeriod qLodgeAvailabilityPeriod = QLodgeAvailabilityPeriod.lodgeAvailabilityPeriod;

    @JsonIgnore
    public BooleanBuilder getPredicate() {
        BooleanBuilder predicate = new BooleanBuilder();

        if (location != null) {
            predicate.and(qLodge.location.containsIgnoreCase(location));
        }
        if (guestNumber != null) {
            predicate.and(qLodge.minimalGuestNumber.loe(guestNumber));
            predicate.and(qLodge.maximalGuestNumber.goe(guestNumber));
        }

        if (dateFrom != null && dateTo != null) {
            BooleanExpression dateRangeCondition = qLodge.id.in(
                    JPAExpressions
                            .select(qLodgeAvailabilityPeriod.lodge.id)
                            .from(qLodgeAvailabilityPeriod)
                            .where(qLodgeAvailabilityPeriod.dateFrom.before(dateTo)
                                    .and(qLodgeAvailabilityPeriod.dateTo.after(dateFrom)))
            );
            predicate.and(dateRangeCondition);
        } else if (dateFrom != null) {
            predicate.and(qLodge.availabilityPeriods.any().dateFrom.after(dateFrom));
        } else if (dateTo != null) {
            predicate.and(qLodge.availabilityPeriods.any().dateTo.before(dateTo));
        }

        return predicate;
    }

}
