# Booking System


---
What I learned:
- @Mapper MapStructLarge projects, 20+ entities, you want zero boilerplate
- -⚠️ Lombok must be listed before MapStruct in annotationProcessorPaths, otherwise MapStruct can't see Lombok-generated getters/setters
```shell
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CityMapper {

    // CityRequest  ──►  City entity
    City toEntity(CityRequest request);

    // City entity  ──►  CityResponse
    CityResponse toResponse(City city);

    // Partial update: only overwrite non-null fields onto existing entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(CityRequest request, @MappingTarget City city);
}
```