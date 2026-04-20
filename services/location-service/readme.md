# location-service
- Responsible for handling all location-related operations, such as fetching location data, updating location information, and managing location-based services.
- Cities
  - geo-location with timezone and region information
- Airport
  - IATA with country code and infos

### Entities type
1. Cities
- id: string
- name: string
- countryCode 
- country 
- cityCode
- city 
- regionCode
2. Airport
- id: string
- iata
- name
- timezoneId
- address
- geoCode --Embedded object
  - latitude
  - longitude
- city  [refreence of cities] 
  - one city have many airports @manytoone 
  - bidirection or unidirectional [each city airport hold reference of city but city hold reference of airport]  ----Bidirectional


### Redis Caching 
Cities and Airport data rarely change and its read very frequenctly 
- Cache annotion 
  - @Cacheable: to cache the result of a method call (airportByiata)
  - @CachePut: to update the cache without interfering with the method execution
  
- cache eviction
  - @CacheEvict: to remove entries from the cache (when city or airport data is updated or deleted)
  - @CacheEvict(allEntities =true)
- cache ttl


### Bulk Operations
- Importing all airport of India
  - POST: api/airports/bulk
  - POST: api/cities/bulk
- Validation failure

### Dto & Mappeer Pattern

### Api EndPoints
#### Cities
base : api/cities
1. Post : create 
2. Post : bulk create /bulk
3. Get : get  all citiest
4. get : get by id {id}
5. Put : update cities by id {id}
6. delete : hard delete city  by id {id}

7. get : search by cititest /search 
#### Airports
base :api/airport
CURD operation  [1 -6 ]
7. get : get by iata code /iata/{iata}
8. get : search by city id-code /city/{city}

