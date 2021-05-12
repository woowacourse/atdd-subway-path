package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
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

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(
                new Member(
                        id,
                        memberRequest.getEmail(),
                        memberRequest.getPassword(),
                        memberRequest.getAge()
                )
        );
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
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
}
