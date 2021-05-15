package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.exception.UnauthorizedException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class AuthService {

    private final JwtTokenProvider tokenProvider;
    private final MemberService memberService;

    public AuthService(JwtTokenProvider tokenProvider, MemberService memberService) {
        this.tokenProvider = tokenProvider;
        this.memberService = memberService;
    }

    public String createAccessToken(String email) {
        if (!memberService.isExist(email)) {
            throw new UnauthorizedException(
                    String.format("해당 이메일로 가입한 유저가 없습니다. 이메일 : %s", email));
        }
        MemberResponse memberByEmail = memberService.findMemberByEmail(email);
        return tokenProvider.createToken(String.valueOf(memberByEmail.getId()));
    }

    public Member getMember(String accessToken) {
        if (!tokenProvider.validateToken(accessToken)) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
        long id = payloadToLong(accessToken);

        MemberResponse member = memberService.findMember(id);
        return member.toMember();
    }

    private long payloadToLong(String accessToken) {
        String payload = tokenProvider.getPayload(accessToken);
        try {
            return Long.parseLong(payload);
        } catch (NumberFormatException e) {
            throw new UnauthorizedException(String.format("토큰의 payload 값이 id가 아닙니다. payload값 : %s", payload));
        }
    }
}
