package wooteco.member.service;

import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wooteco.member.exception.EmailNotFoundException;
import wooteco.member.exception.InvalidPasswordException;
import wooteco.member.controller.dto.request.ApprovedMemberRequest;
import wooteco.member.controller.dto.request.SignInRequest;
import wooteco.member.controller.dto.response.SignInResponse;
import wooteco.member.dao.MemberDao;
import wooteco.member.domain.Member;
import wooteco.member.infrastructure.JwtTokenProvider;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public SignInResponse createToken(SignInRequest signInRequest) {
        Member member = getUserInfo(signInRequest.getEmail());
        if (!passwordEncoder.matches(signInRequest.getPassword(), member.getPassword())) {
            throw new InvalidPasswordException("계정의 비밀번호가 잘못되었습니다.");
        }
        String accessToken = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new SignInResponse(accessToken);
    }

    private Member getUserInfo(String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("해당 이메일에 대한 계정 정보가 없습니다."));
    }

    public ApprovedMemberRequest findMemberByToken(String token) {
        try {
            String payload = jwtTokenProvider.getPayload(token);
            Member member = memberDao.findById(Long.valueOf(payload));
            return ApprovedMemberRequest.from(member);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("토큰에 대한 계정 정보를 찾아오는 과정에서 에러 발생.");
        }
    }

    public void validateToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new JwtException("토큰에 대한 계정 정보를 찾아오는 과정에서 에러 발생.");
        }
    }
}
