package wooteco.subway.auth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService, ObjectMapper objectMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
        this.objectMapper = objectMapper;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        if (memberService.checkInvalidLogin(tokenRequest.getEmail(), tokenRequest.getPassword())) {
            throw new AuthorizationException();
        }

        String accessToken = jwtTokenProvider.createToken(tokenRequest.getEmail());
        return new TokenResponse(accessToken);
    }

    public LoginMember findLoginMember(String token) throws JsonProcessingException {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException();
        }
        return objectMapper.readValue(jwtTokenProvider.getPayload(token),LoginMember.class);
    }
}
