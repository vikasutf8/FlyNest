
# Flight Operations Services


## Enitity 
### Flight
1. id
2. flightNumber
3. airlineId -Long
4. aircraftId
5. departureAirportId
6. arrivalAirportId
7. FlightStatus - status [SCHEDULED, DELAYED, CANCELLED, IN_AIR, LANDED,BOARDING, DEPARTED, ARRIVED,DIVERTED, COMPLETED]
8. createdAt
9. updateAt

### flightInstance

id
arilineId
flight  --- many to one

departureAirportId
arrivalAirportId
scheduleId

departureTime
arrivalTime

totalSeats
availableSeats
FlightStatus status

minAdvanceBookingDays
maxAdvanceBookingDays

isActive

fu string getFormatedDuration() {
    // gap between departure time and arrivaltime ---hrs ,mins
}



