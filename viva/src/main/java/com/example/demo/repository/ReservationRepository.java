package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.entity.Reservation;

//public interface UserRepository extends JpaRepository<Users,String> {
//	  List<Users> findByRole(Role.intr);
//	}

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	/**
	 * Reservation.mem.userId == userId 인 목록을 예약 일시(resReservedDt) 내림차순으로 가져옵니다.
	 */
//	// 예약자(회원) 기준 조회
//	// List<Reservation> findAllByMemIdOrderByResReservedDtDesc(String memId);
//
//	// 회원 기준, 예약일자+시간 내림차순 정렬
//	List<Reservation> findAllByMemIdOrderByReservedDateDescReservedTimeDesc(String memId);
//
//	// 로그인한 회원의 모든 예약 (내림차순)
//	List<Reservation> findAllByMemIdUserIdOrderByDateDescReservedTimeDesc(String userId);
//
//	// 주어진 resId 리스트에 해당하는 예약들만
//	// List<Reservation> findAllByResIdInOrderByResReservedDtDesc(List<String>
//	// resIds);
//
//	// 주어진 resId 목록에 해당하는 예약들만 (예약일자+시간 정렬)
//	List<Reservation> findAllByResIdInOrderByReservedDateDescReservedTimeDesc(List<String> resIds);
//
//	// 면접관 기준 조회
//	// List<Reservation> findAllByIntrIdOrderByResReservedDtDesc(String intrId);
//	// 면접관 기준 조회 (예약일자+시간 정렬)
//	List<Reservation> findAllByIntrIdOrderByReservedDateDescReservedTimeDesc(String intrId);
//
//	// 예약상태
//	List<Reservation> findAllByMemIdAndResStatus(String memId, Reservation.ResStatus resStatus);
	
	  /** 회원(memId) 기준, reservedDate/Time 내림차순 조회 */
    List<Reservation> findAllByMemIdOrderByReservedDateDescReservedTimeDesc(String memId);

    /** 결제된 예약(resId 리스트) // 예약일자/시간 내림차순 */
    List<Reservation> findAllByResIdInOrderByReservedDateDescReservedTimeDesc(List<Long> resIds);

    /** 면접관(intrId) 기준, reservedDate/Time 내림차순 조회 */
    List<Reservation> findAllByIntrIdOrderByReservedDateDescReservedTimeDesc(String intrId);

    /** 특정 회원/상태별 조회 */
    List<Reservation> findAllByMemIdAndResStatus(String memId, String resStatus);
    
//  면접관 ID로 예약 목록 조회
    List<Reservation> findByIntrIdOrderByReservedDateDescReservedTimeDesc(String intrId);
    
 // 면접관 ID로 예약 리스트 가져오기
    List<Reservation> findByIntrId(String intrId);
    
    

 

	


}