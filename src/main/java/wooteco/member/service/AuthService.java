package wooteco.member.service;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wooteco.exception.HttpException;
import wooteco.member.controller.dto.request.SignInRequest;
import wooteco.member.controller.dto.response.SignInResponse;
import wooteco.member.dao.MemberDao;
import wooteco.member.domain.Member;
import wooteco.member.infrastructure.JwtTokenProvider;

@Service
public class AuthService {
    private static final String INVALID_TOKEN_ERROR_MESSAGE = "유효하지 않은 토큰입니다.";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao, PasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
    }

    public SignInResponse createToken(SignInRequest signInRequest) {
        Member member = getUserInfo(signInRequest.getEmail());
        if (passwordEncoder.matches(member.getPassword(), signInRequest.getPassword())) {
            throw new HttpException(HttpStatus.UNAUTHORIZED, "로그인 정보가 올바르지 않습니다.");
        }
        String accessToken = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new SignInResponse(accessToken);
    }

    private Member getUserInfo(String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new HttpException(HttpStatus.UNAUTHORIZED, "이메일이 틀렸습니다."));
    }

    public Member findMemberByToken(String token) {
        try {
            String payload = jwtTokenProvider.getPayload(token);
            return memberDao.findById(Long.valueOf(payload));
        } catch (JwtException | IllegalArgumentException e) {
            throw new HttpException(HttpStatus.UNAUTHORIZED, INVALID_TOKEN_ERROR_MESSAGE);
        }
    }

    public void validateToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new HttpException(HttpStatus.UNAUTHORIZED, INVALID_TOKEN_ERROR_MESSAGE);
        }
    }
}
