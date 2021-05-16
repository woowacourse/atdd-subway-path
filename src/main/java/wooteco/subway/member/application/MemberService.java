package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.DuplicateEmailException;

@Service
public class MemberService {

    private static final int NONE_USER_COUNT = 0;
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = request.toMember();
        if (isExist(member.getEmail())) {
            throw new DuplicateEmailException(String.format("해당 이메일은 이미 등록되어있습니다. 등록된 이메일 : %s", member.getEmail()));
        }
        Member memberWithId = memberDao.insert(member);
        return MemberResponse.of(memberWithId);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberDao.findById(id);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(),
                memberRequest.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }

    public MemberResponse findMemberByEmail(String email) {
        Member member = memberDao.findByEmail(email);
        return MemberResponse.of(member);
    }

    public boolean isExist(String email) {
        long count = memberDao.count(email);
        return count > NONE_USER_COUNT;
    }
}
