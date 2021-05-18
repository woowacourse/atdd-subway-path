package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.member.MemberDao;
import wooteco.subway.domain.member.Member;
import wooteco.subway.web.member.dto.MemberRequest;
import wooteco.subway.web.member.dto.MemberResponse;
import wooteco.subway.exception.MemberNotFoundException;

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
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }
}
