package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.Payload;
import wooteco.subway.exception.member.NotRegisteredMemberException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public Long createMember(MemberRequest request) {
        return memberDao.insert(request.toMember());
    }

    public MemberResponse findMemberById(Long id) {
        Member member = memberDao.findById(id)
                .orElseThrow(NotRegisteredMemberException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse findMemberByPayload(Payload payload) {
        Member member = memberDao.findByEmail(
                    payload.getPayload())
                .orElseThrow(NotRegisteredMemberException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(
                id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }
}
