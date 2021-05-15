package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.domain.LoginMember;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

@Service
public class AuthService {

    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = findMemberByEmailAndPassword(tokenRequest.getEmail(), tokenRequest.getPassword());
        String accessToken = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new TokenResponse(accessToken);
    }

    private void validateToExistsByEmailAndPassword(String email, String password) {
        if (!memberDao.existsByEmailAndPassword(email, password)) {
            throw new AuthenticationException("존재하지 않는 이메일 또는 패스워드 입니다.");
        }
    }

    private Member findMemberByEmailAndPassword(String email, String password) {
        validateToExistsByEmailAndPassword(email, password);
        return memberDao.findByEmail(email);
    }

    private void validateToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException();
        }
    }

    public LoginMember findLoginMemberByToken(String token) {
        validateToken(token);
        Long payload = Long.parseLong(jwtTokenProvider.getPayload(token));
        return new LoginMember(payload);
    }
}
