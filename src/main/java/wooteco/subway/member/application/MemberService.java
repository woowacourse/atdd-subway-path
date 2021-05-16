package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
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

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
        return MemberResponse.of(member);
    }

    public MemberResponse updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
        return new MemberResponse(id, memberRequest.getEmail(), memberRequest.getAge());
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }
}
