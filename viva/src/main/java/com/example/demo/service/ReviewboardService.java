package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Review;
import com.example.demo.model.entity.Reviewboard;
import com.example.demo.repository.ReviewboardRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewboardService {

    private final ReviewboardRepository reviewboardRepository;

    // 모든 리뷰 조회
    public List<Reviewboard> getAllReviews() {
        return reviewboardRepository.findAll();
    }

    // 리뷰 아이디로 조회
    public Reviewboard getReviewById(Long reviewId) {
        return reviewboardRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다. id=" + reviewId));
    }

    // 리뷰 저장
    public Reviewboard saveReview(Reviewboard reviewboard) {
        return reviewboardRepository.save(reviewboard);
    }

    // 리뷰 삭제
    public void deleteReview(Long reviewId) {
        reviewboardRepository.deleteById(reviewId);
    }

    // 삭제
    public void deleteById(Long id) {
        reviewboardRepository.deleteById(id);
    }
	
    // 리뷰 수정
    public Reviewboard findById(Long id) {
        return reviewboardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다. id=" + id));
    }

    @Transactional
    public void updateReview(Reviewboard updatedReview) {
        Reviewboard existing = findById(updatedReview.getReviewId());

        existing.setTitle(updatedReview.getTitle());
        existing.setContent(updatedReview.getContent());
        existing.setRating(updatedReview.getRating());

        // updatedAt 필드는 @PreUpdate에서 자동 갱신됨

        // 별도 저장 호출 필요 없음 (JPA 변경 감지로 자동 반영)
    }

	public List<Reviewboard> findAll() {
		 return reviewboardRepository.findAll();
	}
	// 로그인한 유저가 작성한 리뷰 조회
    public List<Reviewboard> findByUserId(String userId) {
        return reviewboardRepository.findByUser_UserId(userId);
    }
}
