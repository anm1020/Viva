package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.entity.Board;
import com.example.demo.model.entity.Users;
import com.example.demo.service.BoardService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

	private final BoardService boardService;

	// 🔹 게시글 목록
	@GetMapping("/list")
	public String list(Model model, 
						@RequestParam(name = "page", defaultValue = "0") int page,
						@RequestParam(name = "size", defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		Page<Board> boardPage = boardService.getBoardPage(pageable);
		model.addAttribute("boardPage", boardPage); // ✅ 모델 이름 일치
		return "board/list";
	}

	// 🔹 게시글 상세보기
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") Integer id, Model model) {
		boardService.increaseViewCount(id); // 조회수 증가
		Board board = boardService.getBoardById(id);
		model.addAttribute("board", board);
		return "board/view"; // → templates/board/view.html
	}

	// 🔹 게시글 작성 폼
	@GetMapping("/write")
	public String writeForm(Model model) {
		model.addAttribute("board", new Board());
		return "board/write"; // → templates/board/write.html
	}

	// 🔹 게시글 저장 (작성 또는 수정)
	@PostMapping("/write")
	public String write(@ModelAttribute Board board, HttpSession session) {
		// 세션에서 사용자 정보 꺼내기
		Users loginUser = (Users) session.getAttribute("user");

		if (loginUser == null) {
			return "redirect:/login"; // 로그인 안돼있으면 로그인 페이지로
		}

		// 사용자 ID 설정
		board.setUserId(loginUser.getUserId());

		boardService.saveBoard(board);
		return "redirect:/board/list";
	}

	// 수정 폼 (기존 게시글 불러오기)
	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable("id") Integer id, Model model, HttpSession session) {
		Board board = boardService.getBoardById(id);

		// 작성자 본인만 수정 가능
		Users loginUser = (Users) session.getAttribute("user");
		if (loginUser == null || !loginUser.getUserId().equals(board.getUserId())) {
			return "redirect:/board/list"; // 권한 없음
		}

		model.addAttribute("board", board);
		return "board/edit";
	}

	// 수정 처리
	@PostMapping("/edit")
	public String edit(@ModelAttribute Board board, HttpSession session) {
		Users loginUser = (Users) session.getAttribute("user");
		board.setUserId(loginUser.getUserId()); // 다시 넣어줘야 함
		boardService.saveBoard(board); // JPA save는 수정도 처리
		return "redirect:/board/view/" + board.getId();
	}

	// 🔹 게시글 삭제
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		boardService.deleteBoard(id);
		return "redirect:/board/list";
	}
}
