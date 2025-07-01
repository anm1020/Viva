package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dao.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
}
