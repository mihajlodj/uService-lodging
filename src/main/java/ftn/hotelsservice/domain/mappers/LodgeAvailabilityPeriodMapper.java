package ftn.hotelsservice.domain.mappers;

import ftn.hotelsservice.domain.dtos.*;
import ftn.hotelsservice.domain.entities.Lodge;
import ftn.hotelsservice.domain.entities.LodgeAvailabilityPeriod;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LodgeAvailabilityPeriodMapper {

    LodgeAvailabilityPeriodMapper INSTANCE = Mappers.getMapper(LodgeAvailabilityPeriodMapper.class);

    @Mapping(source = "lodge.id", target = "lodgeId")
    LodgeAvailabilityPeriodDto toDto(LodgeAvailabilityPeriod lodgeAvailabilityPeriod);

    @Mapping(source = "lodge.id", target = "lodgeId")
    List<LodgeAvailabilityPeriodDto> toDto(List<LodgeAvailabilityPeriod> lodgeAvailabilityPeriods);

    @Mapping(source = "lodge.id", target = "lodgeId")
    List<LodgeAvailabilityPeriodInterserviceDto> toInterserviceDto(List<LodgeAvailabilityPeriod> lodgeAvailabilityPeriods);

    LodgeAvailabilityPeriod fromCreateRequest(LodgeAvailabilityPeriodCreateRequest request);


    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "lodge")
    void update(@MappingTarget LodgeAvailabilityPeriod lodgeAvailabilityPeriod, LodgeAvailabilityPeriodUpdateRequest request);

}
