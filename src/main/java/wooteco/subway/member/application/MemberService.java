package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.auth.domain.LoginMember;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Service
@Transactional
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    private void validateToExistsId(Long id) {
        if (!memberDao.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 id 입니다.");
        }
    }

    @Transactional(readOnly = true)
    public MemberResponse findMember(Long id) {
        validateToExistsId(id);
        Member member = memberDao.findById(id);
        return MemberResponse.of(member);
    }

    public MemberResponse updateMember(Long id, MemberRequest memberRequest) {
        validateToExistsId(id);
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(),
            memberRequest.getAge()));
        Member updatedMember = memberDao.findById(id);
        return MemberResponse.of(updatedMember);
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }

    @Transactional(readOnly = true)
    public MemberResponse findMemberOfMine(LoginMember loginMember) {
        return findMember(loginMember.getId());
    }

    public MemberResponse updateMemberOfMine(LoginMember loginMember, MemberRequest memberRequest) {
        return updateMember(loginMember.getId(), memberRequest);
    }

    public void deleteMemberOfMine(LoginMember loginMember) {
        deleteMember(loginMember.getId());
    }
}
