package com.example.demo.service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entity.PointExchange;
import com.example.demo.repository.PointExchangeRepository;
import com.example.demo.repository.UserPointRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PointExchangeService {

    private final PointExchangeRepository pointExchangeRepository;
    private final UserPointRepository userPointRepository;

    /**
     * 환전 신청 처리
     * @param pointExchange 환전 신청 정보 (userId, amount 등 포함)
     */
    @Transactional
    public void requestExchange(PointExchange pointExchange) {
        String userId = pointExchange.getUserId();
        int amount = pointExchange.getAmount();

        // 1. 사용자 현재 포인트 조회
        int currentPoint = userPointRepository.findById(userId)
            .map(up -> up.getPoint())
            .orElseThrow(() -> new IllegalArgumentException("사용자 포인트 정보가 없습니다."));

        // 2. 잔액 부족 체크
        if (amount > currentPoint) {
            throw new IllegalArgumentException("포인트 잔액이 부족합니다.");
        }

        // 3. 신청 시간 설정
        pointExchange.setRequestedAt(LocalDateTime.now());

        // 4. 상태 기본값 설정
        if (pointExchange.getStatus() == null) {
            pointExchange.setStatus("PENDING");
        }

        // 5. 환전 신청 저장
        pointExchangeRepository.save(pointExchange);

        // 6. 포인트 차감
        int updated = userPointRepository.deductPoint(userId, amount);
        if (updated == 0) {
            throw new IllegalArgumentException("포인트 차감에 실패했습니다.");
        }
    }
    //조회
    public List<PointExchange> getUserExchangeList(String userId) {
    	 List<PointExchange> list = pointExchangeRepository.findByUserIdOrderByRequestedAtDesc(userId);
    	    System.out.println("환전 내역 개수: " + (list == null ? 0 : list.size()));
    return pointExchangeRepository.findByUserIdOrderByRequestedAtDesc(userId);
    }
    
    
    /**
     * 환전 승인 처리 (status = APPROVED)
     */
    public void approveExchange(Long id) {
        PointExchange ex = pointExchangeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("요청이 없습니다: " + id));
        ex.setStatus("APPROVED");
        // @Transactional 클래스 레벨이므로, 커밋 시점에 자동으로 UPDATE 됩니다.
    }

    /**
     * 환전 거절 처리 (status = REJECTED)
     */
    public void rejectExchange(Long id) {
        PointExchange ex = pointExchangeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("요청이 없습니다: " + id));
        ex.setStatus("REJECTED");
    }

    /**
     * 페이징 포함 전체 요청 조회
     * @param page 0부터 시작하는 페이지 번호
     * @param size 페이지당 항목 수
     */
    @Transactional(readOnly = true)
    public Page<PointExchange> getRequests(int page, int size) {
        Page<PointExchange> result = pointExchangeRepository.findAll(
            PageRequest.of(page, size, Sort.by("requestedAt").descending())
        );
        System.out.println("총 건수: " + result.getTotalElements());
        result.getContent().forEach(System.out::println);
        return result;
    }
    
    // 예원 추가
    // 1) 대기 중 요청 조회
    /** 대기 중뿐 아니라 모든 요청을 날짜 내림차순으로 조회 */
//    @Transactional(readOnly = true)
//    public List<PointExchange> getAllRequests() {
//        return pointExchangeRepository.findAll(Sort.by(Sort.Direction.DESC, "requestedAt"));
//    }

    
    //여기서부터
//    public void approveExchange(Long id) {
//        PointExchange ex = pointExchangeRepository.findById(id)
//            .orElseThrow(() -> new IllegalArgumentException("요청이 없습니다: " + id));
//        ex.setStatus("APPROVED");
//    }
//    
//    
//
//    public void rejectExchange(Long id) {
//        PointExchange ex = pointExchangeRepository.findById(id)
//            .orElseThrow(() -> new IllegalArgumentException("요청이 없습니다: " + id));
//        ex.setStatus("REJECTED");
//    }
//    
//    
//    /** 페이징 포함 전체 요청 조회 */
//    @Transactional(readOnly = true)
//    public Page<PointExchange> getRequests(int page, int size) {
//        return pointExchangeRepository.findAll(
//            PageRequest.of(page, size, Sort.by("requestedAt").descending())
//        );
//    } 요까지
    
//    // 예원추가. 관리자  Pending 상태 요청만 조회
//    public List<PointExchange> getPendingRequests() {
//        return pointExchangeRepository.findByStatusOrderByRequestedAtDesc("PENDING");
//    }
//    
//    
//     // 관리자용: 환전 요청 상태 변경 (APPROVED / REJECTED)
//    @Transactional
//    public void approveExchange(Long exchangeId, String status) {
//        PointExchange exchange = pointExchangeRepository.findById(exchangeId)
//            .orElseThrow(() -> new IllegalArgumentException(
//                "환전 요청이 존재하지 않습니다: " + exchangeId));
//
//        exchange.setStatus(status);  // "APPROVED" 가 넘어옵니다.
//        // 굳이 save() 호출 안 하셔도 JPA Dirty Checking 으로 자동 반영됩니다,
//        // 그래도 명시적으로 다시 저장하고 싶다면 아래 한 줄 추가:
//        // pointExchangeRepository.save(exchange);
//    }
//
//    @Transactional
//    public void rejectExchange(Long exchangeId, String status) {
//        PointExchange exchange = pointExchangeRepository.findById(exchangeId)
//            .orElseThrow(() -> new IllegalArgumentException(
//                "환전 요청이 존재하지 않습니다: " + exchangeId));
//
//        exchange.setStatus(status);  // "REJECTED" 가 넘어옵니다.
//        // pointExchangeRepository.save(exchange);
//
//        // 선택: 거절 시 차감된 포인트를 환급하려면 아래 로직 추가
//        // userPointRepository.addPoint(exchange.getUserId(), exchange.getAmount());
//    }
    
    
    // 관리자용: 환전 요청 승인
//    @Transactional
//    public void approveExchange(Long exchangeId) {
//        PointExchange exchange = pointExchangeRepository.findById(exchangeId)
//            .orElseThrow(() -> new IllegalArgumentException(
//                "환전 요청이 존재하지 않습니다: " + exchangeId));
//
//        // 상태 변경
//        exchange.setStatus("APPROVED");
//        pointExchangeRepository.save(exchange);
//    }
//    
//    // 관리자용: 환전 요청 거절
//    @Transactional
//    public void rejectExchange(Long exchangeId) {
//        PointExchange exchange = pointExchangeRepository.findById(exchangeId)
//            .orElseThrow(() -> new IllegalArgumentException(
//                "환전 요청이 존재하지 않습니다: " + exchangeId));
//
//        // 상태 변경
//        exchange.setStatus("REJECTED");
//        pointExchangeRepository.save(exchange);

}
