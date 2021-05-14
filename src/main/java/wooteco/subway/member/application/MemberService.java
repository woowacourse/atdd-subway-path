package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.TokenRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.InvalidMemberException;

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

    @Transactional
    public void updateMember(String email, Member member) {
        Member originMember = memberDao.findByEmail(email);
        memberDao.update(
                new Member(
                        originMember.getId(),
                        member.getEmail(),
                        member.getPassword(),
                        member.getAge()
                )
        );
    }

    public void deleteMember(String email) {
        memberDao.deleteByEmail(email);
    }

    public void authenticate(TokenRequest tokenRequest) {
        boolean isValid = memberDao.checkFrom(
                tokenRequest.getEmail(),
                tokenRequest.getPassword()
        );

        if (!isValid) {
            throw new InvalidMemberException();
        }
    }

    public Member findByEmail(String email) {
        return memberDao.findByEmail(email);
    }
}
