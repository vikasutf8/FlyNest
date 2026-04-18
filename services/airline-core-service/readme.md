

# Airline-core


## Entity: Airline
    id
    iataCode;
    icaoCode;
    EnableResilientMethods
    alias;
    logoUrl;
    website;
    AirlineStatus status [ACTIVE ,INACTIVE ,BANNED]
    alliances
    headquartersCityId
    updateById
    createdAt
    updatedAt

## Entity : AirCraft
1. id
2. code - unique code , not null
3. model -not null
4. manufacturer - not null
5. seatingCapacity
6. economySeats
7. permiumEconomySeats
8. businessSeats
9. firstClassSeats
10. curisingSpeedKmh
11. yearOfManufacture
12. registrationDate
13. nextMaintanceDate
12. AirCraftStatus  status [ACIVE,FLYING,MAINTENANCE,DECOMMISSIONED]
13. isAvailable
14. Airline airline --Manytoone --one airline having multiple aircrafts
15. currentAirportId
16. createdAt
17. updatedAt

function getTotalSeats(){
sum of type of seats
economySeats
permiumEconomySeats
businessSeats
firstClassSeats
}


funtion isOperational(){
if status is active and isAvailable is true then return true else false
}

function boolean requiredMaintances(){
return true if nextMaintanceDate is less than current date + 2 week (within 2 weeks) else false
}