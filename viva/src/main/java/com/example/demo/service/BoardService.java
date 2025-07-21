package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.entity.Board;
import com.example.demo.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;

	// 전체 게시글 목록 조회
	public List<Board> getAllBoards() {
		return boardRepository.findAll();
	}

	// 게시글 상세 조회 (id 기준) - 댓글 포함
	@Transactional(readOnly = true)
	public Board getBoardById(Integer id) {
		Optional<Board> boardOpt = boardRepository.findById(id);
		if (boardOpt.isPresent()) {
			Board board = boardOpt.get();
			// 댓글을 즉시 로딩 (LAZY 로딩 방지)
			board.getComments().size();
			return board;
		}
		return null;
	}

	// 게시글 저장 (등록/수정)
	@Transactional
	public void saveBoard(Board board) {
		boardRepository.save(board);
	}

	// 게시글 삭제
	@Transactional
	public void deleteBoard(Integer id) {
		boardRepository.deleteById(id);
	}

	// 조회수 증가 (트랜잭션 보장)
	@Transactional
	public void increaseViewCount(Integer id) {
		boardRepository.findById(id).ifPresent(board -> {
			board.setViewCount(board.getViewCount() + 1);
			boardRepository.save(board); // 수정 반영
		});
	}

	//  페이징된 게시글 목록 반환
    public Page<Board> getBoardPage(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }
}
