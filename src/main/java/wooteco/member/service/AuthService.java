package wooteco.member.service;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import wooteco.exception.HttpException;
import wooteco.member.controller.dto.request.LoginRequestDto;
import wooteco.member.controller.dto.response.LoginTokenResponseDto;
import wooteco.member.dao.MemberDao;
import wooteco.member.domain.Member;
import wooteco.member.infrastructure.JwtTokenProvider;

@Service
public class AuthService {
    private static final String INVALID_TOKEN_ERROR_MESSAGE = "유효하지 않은 토큰입니다.";
    private static final String MEMBER_NOT_FOUND_ERROR_MESSAGE = "사용자가 존재하지 않습니다.";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public LoginTokenResponseDto createToken(LoginRequestDto loginRequestDto) {
        Member member = getUserInfo(loginRequestDto.getEmail());
        member.validatePassword(loginRequestDto.getPassword());
        String accessToken = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new LoginTokenResponseDto(accessToken);
    }

    private Member getUserInfo(String principal) {
        return memberDao.findByEmail(principal)
            .orElseThrow(() -> new HttpException(HttpStatus.UNAUTHORIZED, MEMBER_NOT_FOUND_ERROR_MESSAGE));
    }

    public Member findMemberByToken(String token) {
        try {
            String payload = jwtTokenProvider.getPayload(token);
            return memberDao.findById(Long.valueOf(payload))
                .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, MEMBER_NOT_FOUND_ERROR_MESSAGE));
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
