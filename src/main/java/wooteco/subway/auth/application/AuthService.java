package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.exception.InvalidMemberInformationException;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMemberId;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public String createToken(final TokenRequest tokenRequest) {
        if (memberService.containsMemberByEmailAndPassword(tokenRequest.getEmail(),
            tokenRequest.getPassword())) {
            return jwtTokenProvider.createToken(tokenRequest.getEmail());
        }
        throw new InvalidMemberInformationException();
    }

    public boolean isValidToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    public LoginMemberId findLoginMemberId(String token) {
        if (!isValidToken(token)) {
            throw new InvalidMemberInformationException();
        }
        String payLoad = jwtTokenProvider.getPayload(token);
        return new LoginMemberId(memberService.findMemberIdByEmail(payLoad));
    }

}
