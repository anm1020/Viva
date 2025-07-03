package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VideoRoomController {
	
	@GetMapping("/videoroom")
	public String showVideoRoomTest(Model model) {
		return "webRtc/videoroomtestThyme";
	}
	
}
