package wooteco.subway.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import wooteco.subway.config.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.controller.response.MemberResponse;
import wooteco.subway.dto.LoginMember;

@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String issueToken(String email, String password) {
        MemberResponse memberResponse = memberService.logIn(email, password);
        return jwtTokenProvider.createToken(memberResponse.getId(), memberResponse.getEmail(), memberResponse.getAge());
    }

    public LoginMember parseToken(String token) {
        Claims payloads = jwtTokenProvider.getPayloads(token);
        Long id = payloads.get("id", Long.class);
        String email = payloads.get("email", String.class);
        Integer age = payloads.get("age", Integer.class);
        return new LoginMember(id, email, age);
    }

    public void validate(String token) {
        jwtTokenProvider.validateToken(token);
    }
}
