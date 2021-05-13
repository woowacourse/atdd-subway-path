package wooteco.subway.controller;

import wooteco.subway.service.MemberService;

public class AuthController {
    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    // TODO: 로그인(토큰 발급) 요청 처리하기
    // todo: login에 대한 postMapping
    // MemberService
}
