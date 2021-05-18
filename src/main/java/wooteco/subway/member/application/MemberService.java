package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.NotExistMemberException;
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
        Member member = memberDao.findById(id).orElseThrow(NotExistMemberException::new);
        return MemberResponse.of(member);
    }

    public Long findMemberIdByEmail(String email) {
        Long memberId = memberDao.findIdByEmail(email);
        if (memberId != null) {
            return memberId;
        }
        throw new NotExistMemberException();
    }

    public boolean containsMemberByEmailAndPassword(String email, String password) {
        return memberDao.containsMemberByEmailAndPassword(email, password);
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(),
            memberRequest.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }
}
