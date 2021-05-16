package wooteco.subway.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.LoginMember;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.service.MemberService;

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
        return jwtTokenProvider.createToken(memberResponse.getId(), memberResponse.getEmail());
    }

    public LoginMember parseToken(String token) {
        Claims payloads = jwtTokenProvider.getPayloads(token);
        Long id = payloads.get("id", Long.class);
        String email = payloads.get("email", String.class);
        return new LoginMember(id, email);
    }

    public void validate(String token) {
        jwtTokenProvider.validateToken(token);
    }
}
