package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.exception.InvalidMemberException;
import wooteco.subway.exception.InvalidTokenException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class MemberService {
    private final AuthService authService;
    private final MemberDao memberDao;

    public MemberService(AuthService authService, MemberDao memberDao) {
        this.authService = authService;
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberDao.findById(id);
        return MemberResponse.of(member);
    }

    public MemberResponse findMemberByEmail(String token) {
        if (authService.validateToken(token)) {
            String email = authService.getPayload(token);
            return MemberResponse.of(memberDao.findByEmail(email));
        }
        throw new InvalidTokenException();
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void updateMember(Long id, MemberRequest memberRequest, String token) {
        if (authService.validateToken(token)) {
            memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
            return;
        }
        throw new InvalidTokenException();
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }

    public void deleteMember(Long id, String token) {
        if (authService.validateToken(token)) {
            memberDao.deleteById(id);
            return;
        }
        throw new InvalidTokenException();
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        validateEmailAndPassword(tokenRequest);
        return new TokenResponse(authService.createToken(tokenRequest.getEmail()));
    }

    private void validateEmailAndPassword(TokenRequest tokenRequest) {
        memberDao.findByEmailAndPassword(tokenRequest.getEmail(), tokenRequest.getPassword())
                .orElseThrow(() -> new InvalidMemberException(tokenRequest.getEmail()));
    }
}
