package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class MemberService {
    private MemberDao memberDao;
    private AuthService authService;

    public MemberService(MemberDao memberDao, AuthService authService) {
        this.memberDao = memberDao;
        this.authService = authService;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberDao.findById(id);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }

    public MemberResponse findMemberByToken(String token) {
        String payload = authService.getPayload(token);
        Member member = memberDao.findByEmail(payload);
        return MemberResponse.of(member);
    }

    public boolean doesEmailExist(String email) {
        return memberDao.doesEmailExist(email);
    }

    public boolean doesPasswordExist(String password) {
        return memberDao.doesPasswordExist(password);
    }
}
