package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.ObjectNotFoundException;
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
        return MemberResponse.from(member);
    }

    public MemberResponse findMember(Long id) {
        return MemberResponse.from(
            memberDao.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("유저를 찾는데 실패했습니다."))
        );
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }
}
