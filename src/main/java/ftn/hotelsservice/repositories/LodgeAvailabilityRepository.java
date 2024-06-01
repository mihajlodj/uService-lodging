package ftn.hotelsservice.repositories;

import ftn.hotelsservice.domain.entities.LodgeAvailabilityPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LodgeAvailabilityRepository extends JpaRepository<LodgeAvailabilityPeriod, UUID>, QuerydslPredicateExecutor<LodgeAvailabilityPeriod> {

    List<LodgeAvailabilityPeriod> findByLodgeId(UUID lodgeId);

}
