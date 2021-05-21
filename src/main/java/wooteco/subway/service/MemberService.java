package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.MemberDao;
import wooteco.subway.domain.Member;
import wooteco.subway.exception.MemberNotFoundException;
import wooteco.subway.web.request.MemberRequest;
import wooteco.subway.web.response.MemberResponse;

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

    public MemberResponse findMemberByEmail(String email) {
        final Member member = memberDao.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(email));
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }
}
