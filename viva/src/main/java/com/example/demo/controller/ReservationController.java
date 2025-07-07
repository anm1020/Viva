package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.ReservationDTO;
import com.example.demo.service.ReservationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

	 private final ReservationService service;
	 
	    /**
	     * 1) 특정 면접관의 날짜별 예약 가능 시간 조회
	     */
//	    @GetMapping("/availability")
//	    public List<LocalTime> getAvailability(@RequestParam String interviewerId,
//	    									   @RequestParam 
//	    									   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
//	    ) {
//	        return service.findAvailableSlots(interviewerId, date);
//	    }

	    /**
	     * 2) 예약 생성 (구직자→면접관)
	     */
//	    @PostMapping
//	    public ReservationDTO createReservation(@RequestBody ReservationDTO dto) {
//	        return service.book(dto);
//	    }


	// ① 가능 시간 조회 → GET  /api/interviewers/{id}/availability?date=YYYY-MM-DD
	  @GetMapping("/{id}/availability")
	  public List<String> availability(
	      @PathVariable("id") String interviewerId,
	      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	  ) {
	    return service.findAvailableSlots(interviewerId, date)
	                  .stream()
	                  .map(LocalTime::toString)
	                  .collect(Collectors.toList());
	  }

	  // ② 예약 생성 → POST /api/interviewers/{id}/reservations
	  @PostMapping("/{id}/reservations")
	  public Map<String,Object> book(
	      @PathVariable("id") String interviewerId,
	      @RequestBody ReservationDTO dto
	  ) {
	    dto.setResIntrId(interviewerId);
	    ReservationDTO result = service.book(dto);
	    return Map.of(
	      "success", result != null,
	      "reservation", result,
	      "message", result != null ? "OK" : "이미 예약되었거나 실패"
	    );
	  }
}
