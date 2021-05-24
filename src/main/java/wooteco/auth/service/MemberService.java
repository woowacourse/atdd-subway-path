package wooteco.auth.service;

import org.springframework.stereotype.Service;
import wooteco.auth.dao.MemberDao;
import wooteco.auth.domain.Member;
import wooteco.auth.web.dto.MemberRequest;
import wooteco.auth.web.dto.MemberResponse;
import wooteco.exception.notfound.MemberNotFoundException;

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
        Member member = memberDao.findById(id).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }
}
