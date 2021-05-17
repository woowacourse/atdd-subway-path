package wooteco.subway.auth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.AuthorizationException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider,
        ObjectMapper objectMapper) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        MemberResponse memberResponse = memberService
            .findMemberByEmailAndPassword(tokenRequest.getEmail(), tokenRequest.getPassword());
        String token = jwtTokenProvider.createToken(memberResponse);
        return new TokenResponse(token);
    }

    public MemberResponse findLoginMemberByToken(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new AuthorizationException("유효하지 않은 토큰입니다.");
        }

        try {
            return objectMapper.readValue(jwtTokenProvider.getPayload(accessToken), MemberResponse.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
