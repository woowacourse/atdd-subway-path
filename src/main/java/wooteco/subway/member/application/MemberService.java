package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.auth.exception.LoginFailEmailException;
import wooteco.subway.auth.exception.LoginWrongPasswordException;
import wooteco.subway.member.exception.NotRegisteredMemberException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class MemberService {

    private final JwtTokenProvider tokenProvider;
    private final MemberDao memberDao;

    public MemberService(JwtTokenProvider tokenProvider, MemberDao memberDao) {
        this.tokenProvider = tokenProvider;
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberDao.findById(id).orElseThrow(NotRegisteredMemberException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse findMemberByEmail(String email) {
        Member member = memberDao.findByEmail(email).orElseThrow(NotRegisteredMemberException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse findMemberByEmailToken(String token) {
        String email = tokenProvider.getPayload(token);
        return findMemberByEmail(email);
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(),
            memberRequest.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        String email = tokenRequest.getEmail();
        String accessToken = tokenProvider.createToken(email);
        return new TokenResponse(accessToken);
    }

    private void checkAvailableLogin(TokenRequest tokenRequest) {
        String email = tokenRequest.getEmail();
        String password = tokenRequest.getPassword();
        Member member = memberDao.findByEmail(email).orElseThrow(LoginFailEmailException::new);
        if (!member.isSamePassword(password)) {
            throw new LoginWrongPasswordException();
        }
    }
}
