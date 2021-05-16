package wooteco.subway.member.application;

import java.util.Optional;
import org.springframework.stereotype.Service;
import wooteco.subway.auth.exception.AuthorizationException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

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

    public MemberResponse findMemberByEmail(String email) {
        final Member member = memberDao.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(),
            memberRequest.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }

    public void validateMemberAndPassword(String email, String password) {
        Member member = memberDao.findByEmail(email)
            .orElseThrow(() -> new AuthorizationException("이메일이 존재하지 않습니다."));

        if (!member.isEqualToPassword(password)) {
            throw new AuthorizationException("비밀번호가 틀렸습니다!");
        }
    }

    public MemberResponse findByEmail(String email) {
        Member member = memberDao.findByEmail(email)
            .orElseThrow(() -> new AuthorizationException("이메일이 존재하지 않습니다."));
        return MemberResponse.of(member);
    }
}
