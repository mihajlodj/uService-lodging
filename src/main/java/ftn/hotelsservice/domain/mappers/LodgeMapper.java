package ftn.hotelsservice.domain.mappers;

import ftn.hotelsservice.domain.dtos.LodgeCreateRequest;
import ftn.hotelsservice.domain.dtos.LodgeDto;
import ftn.hotelsservice.domain.entities.Lodge;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LodgeMapper {

    LodgeMapper INSTANCE = Mappers.getMapper(LodgeMapper.class);

    LodgeDto toDto(Lodge lodge);

    Lodge fromCreateRequest(LodgeCreateRequest requeest);


}
