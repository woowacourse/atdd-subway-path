package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Transactional
@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(final MemberRequest request) {
        validateEmail(request.getEmail());
        final Member member = memberDao.insert(request.toMember());

        return MemberResponse.of(member);
    }

    public void updateMember(final Long id, final MemberRequest request) {
        validateEmail(request.getEmail());

        memberDao.update(request.toMember(id));
    }

    private void validateEmail(final String email) {
        if (memberDao.isExistEmail(email)) {
            throw new MemberException("이미 존재하는 Email 입니다.");
        }
    }

    public void deleteMember(final Long id) {
        memberDao.deleteById(id);
    }

    public Member findMember(final Long id) {
        return memberDao.findById(id)
                .orElseThrow(() -> new MemberException("존재하지 않는 유저 id 입니다."));
    }
}
