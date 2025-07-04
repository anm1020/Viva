package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.Reservation;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	 List<Reservation> findAllByResIntrIdAndResReservedDtBetween(  
	String resIntrId,
	    LocalDateTime start,
	    LocalDateTime end
	  );
}