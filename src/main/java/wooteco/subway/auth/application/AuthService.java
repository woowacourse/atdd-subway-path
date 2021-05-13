package wooteco.subway.auth.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.exception.AuthorizationException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import java.util.Optional;

import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat.EMAIL;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = memberDao.findByEmailAndPassword(tokenRequest.getEmail(), tokenRequest.getPassword())
                .orElseThrow(() -> new AuthorizationException("[ERROR] 로그인 실패입니다."));
        String accessToken = jwtTokenProvider.createToken(String.valueOf(member.getId()));
        return new TokenResponse(accessToken);
    }

    public boolean checkInvalidLogin(String principal, String credentials) {
        return !memberDao.findByEmailAndPassword(principal, credentials).isPresent();
    }

    public MemberResponse findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    private MemberResponse findMember(String payload) {
        Member member = memberDao.findById(Long.valueOf(payload)).orElseThrow(() ->
                new IllegalArgumentException("[ERROR] 존재하지 않는 회원입니다."));
        return new MemberResponse(member);
    }

    public void updateMemberByToken(String token, MemberRequest memberRequest) {
        MemberResponse memberResponse = findMemberByToken(token);
        Member member = new Member(memberResponse.getId(), memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge());
        memberDao.update(member);
    }
}
