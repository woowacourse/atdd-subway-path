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

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        validateEmail(request.getEmail());

        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public Member findMember(Long id) {
        return memberDao.findById(id)
                .orElseThrow(()-> new MemberException("존재하지 않는 유저 id입니다."));
    }

    public void updateMember(Long id, MemberRequest request) {
        validateEmail(request.getEmail());

        memberDao.update(new Member(id, request.getEmail(), request.getPassword(), request.getAge()));
    }

    private void validateEmail(final String email) {
        if (memberDao.isExistEmail(email)) {
            throw new MemberException("이미 존재하는 Email 입니다.");
        }
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }
}
