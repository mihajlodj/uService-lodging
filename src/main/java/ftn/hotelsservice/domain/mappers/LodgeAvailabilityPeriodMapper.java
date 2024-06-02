package ftn.hotelsservice.domain.mappers;

import ftn.hotelsservice.domain.dtos.LodgeAvailabilityPeriodCreateRequest;
import ftn.hotelsservice.domain.dtos.LodgeAvailabilityPeriodDto;
import ftn.hotelsservice.domain.dtos.LodgeAvailabilityPeriodUpdateRequest;
import ftn.hotelsservice.domain.entities.LodgeAvailabilityPeriod;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LodgeAvailabilityPeriodMapper {

    LodgeAvailabilityPeriodMapper INSTANCE = Mappers.getMapper(LodgeAvailabilityPeriodMapper.class);

    @Mapping(source = "lodge.id", target = "lodgeId")
    LodgeAvailabilityPeriodDto toDto(LodgeAvailabilityPeriod lodgeAvailabilityPeriod);

    LodgeAvailabilityPeriod fromCreateRequest(LodgeAvailabilityPeriodCreateRequest request);


    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "lodge")
    void update(@MappingTarget LodgeAvailabilityPeriod lodgeAvailabilityPeriod, LodgeAvailabilityPeriodUpdateRequest request);

}
