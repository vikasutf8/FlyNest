package com.flynest.flight_ops_service.service.Impl;

import com.flynest.enums.FlightStatus;
import com.flynest.flight_ops_service.mapper.FlightScheduleMapper;
import com.flynest.flight_ops_service.model.Flight;
import com.flynest.flight_ops_service.model.FlightSchedule;
import com.flynest.flight_ops_service.repository.FlightRepository;
import com.flynest.flight_ops_service.repository.FlightScheduleRepository;
import com.flynest.flight_ops_service.service.FlightInstanceService;
import com.flynest.flight_ops_service.service.FlightScheduleService;
import com.flynest.payload.request.FlightInstanceRequest;
import com.flynest.payload.request.FlightScheduleRequest;
import com.flynest.payload.response.FlightScheduleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FlightScheduleServiceImpl implements FlightScheduleService {

    private final FlightScheduleRepository flightScheduleRepository;
    private final FlightRepository flightRepository;
    private final FlightInstanceService flightInstanceService;

    @Override
    public FlightScheduleResponse createFlightSchedule(FlightScheduleRequest flightScheduleRequest, Long userId) {

        Long airlineId = userId; // Assuming userId is same as airlineId, adjust if needed
        Flight flight =flightRepository.findById(flightScheduleRequest.getFlightId())
                .orElseThrow(() -> new IllegalArgumentException("Flight not found with id: " + flightScheduleRequest.getFlightId()));
        //endData >> startData
        if(flightScheduleRequest.getEndDate().isBefore(flightScheduleRequest.getStartDate())){
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        FlightSchedule flightSchedule = FlightScheduleMapper.toEntity(flightScheduleRequest, flight);
        FlightSchedule fs =flightScheduleRepository.save(flightSchedule);
        // create flight instance for saved schedule

        List<DayOfWeek> operatingDays = flightScheduleRequest.getOperatingDays();
        LocalDate startDate = flightScheduleRequest.getStartDate();
        LocalDate endData = flightScheduleRequest.getEndDate();

        FlightInstanceRequest fir = FlightInstanceRequest.builder()
                .scheduleId(fs.getId())
                .flightId(fs.getFlight().getId())
                .arrivalAirportId(fs.getArrivalAirportId())
                .departureAirportId(fs.getDepartureAirportId())
                .status(FlightStatus.SCHEDULED)
                .build();
        for(LocalDate date =startDate ; !date.isAfter(endData) ; date=date.plusDays(1)){
            if(operatingDays.contains(date.getDayOfWeek())) {
                fir.setDepartureTime(date.atTime(fs.getDepartureTime().atZone(java.time.ZoneId.systemDefault()).toLocalTime()));
                fir.setArrivalTime(date.atTime(fs.getArrivalTime().atZone(java.time.ZoneId.systemDefault()).toLocalTime()));

                flightInstanceService.createFlightInstance(airlineId, fir);
            }}
        return FlightScheduleMapper.toResponse(fs, null, null);
    }

    @Override
    public FlightScheduleResponse getFlightScheduleById(Long id) {
        FlightSchedule flightSchedule = flightScheduleRepository.findById(id).orElseThrow();

        return FlightScheduleMapper.toResponse(flightSchedule, null, null);
    }

    @Override
    public List<FlightScheduleResponse> getAllFlightSchedulesByAirline(Long userId) {
        return flightScheduleRepository.findByFlightAirlineId(userId).stream()
                .map(fs -> FlightScheduleMapper.toResponse(fs, null, null))
                .toList();
    }

    @Override
    public FlightScheduleResponse updateFlightSchedule(Long id, FlightScheduleRequest flightScheduleRequest) {
        FlightSchedule existing = flightScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("FlightSchedule not found with id: " + id));
        // Update entity
        FlightScheduleMapper.updateEntity(existing, flightScheduleRequest);

        // Save
        FlightSchedule saved = flightScheduleRepository.save(existing);
        return FlightScheduleMapper.toResponse(saved, null, null);
    }

    @Override
    public void deleteFlightSchedule(Long id) {
        if (!flightScheduleRepository.existsById(id)) {
            throw new IllegalArgumentException("FlightSchedule not found with id: " + id);
        }
        flightScheduleRepository.deleteById(id);
        log.info("Deleted flight schedule id={}", id);
    }
}
