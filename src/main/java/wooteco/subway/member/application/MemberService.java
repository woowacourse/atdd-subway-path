package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
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

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }

    public MemberResponse findMemberByEmail(final String email) {
        final Member member = memberDao.findByEmail(email).orElseThrow(() -> new AuthorizationException("이메일 또는 비밀번호가 틀립니다."));;
        return MemberResponse.of(member);
    }

    public void authorize(final TokenRequest tokenRequest) {
        final String email = tokenRequest.getEmail();
        final Member member = memberDao.findByEmail(email).orElseThrow(() -> new AuthorizationException("이메일 또는 비밀번호가 틀립니다."));;
        member.authorize(tokenRequest);
    }
}
