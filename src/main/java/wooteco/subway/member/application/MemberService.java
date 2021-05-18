package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.infrastructure.Sha256Hasher;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class MemberService {

    private static final Sha256Hasher HASHER = new Sha256Hasher();

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member memberForCreate = request.toMember().newInstanceWithHashPassword(HASHER::hashing);
        Member savedMember = memberDao.insert(memberForCreate);
        return MemberResponse.of(savedMember);
    }

    public MemberResponse findMemberByEmail(String email) {
        Member member = memberDao.findByEmail(email);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        Member memberForUpdate = new Member(
                id,
                memberRequest.getEmail(),
                memberRequest.getAge());

        memberDao.update(memberForUpdate);
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }
}
