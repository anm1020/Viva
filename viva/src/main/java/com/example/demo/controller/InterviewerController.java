// src/main/java/com/example/demo/controller/InterviewerController.java
package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.dto.Interviewer;

import lombok.RequiredArgsConstructor;

@Controller                         // <— 반드시 붙여야 합니다!
@RequestMapping("/interviewers")    // 뷰용 기본 URL
@RequiredArgsConstructor
public class InterviewerController {

  /** 0) 테스트용 면접관 리스트 */
  @GetMapping
  public String list(Model model) {
    List<Interviewer> list = List.of(
      new Interviewer("intr1","홍길동"),
      new Interviewer("intr2","김영희"),
      new Interviewer("intr3","이철수")
    );
    model.addAttribute("interviewers", list);
    return "interviewer/list";  // src/main/resources/templates/interviewer/list.html
  }

  /** 1) 상세페이지 매핑 (나중에 달력 모달용) */
  @GetMapping("/{intrId}")
  public String detail(@PathVariable String intrId, Model model) {
    model.addAttribute("intrId", intrId);
    return "interviewer/detail"; // 나중에 만들 detail.html
  }
}
