package ftn.hotelsservice.repositories;

import ftn.hotelsservice.domain.entities.Lodge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LodgeRepository extends JpaRepository<Lodge, UUID>, QuerydslPredicateExecutor<Lodge> {

    List<Lodge> findByOwnerId(UUID ownerId);

}
