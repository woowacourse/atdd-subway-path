package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import java.util.Optional;

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

    public MemberResponse findMember(String email) {
        final Optional<Member> member = memberDao.findByEmail(email);
        if (!member.isPresent()) {
            throw new AuthorizationException();
        }
        return MemberResponse.of(member.get());
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void updateMember(String email, MemberRequest memberRequest) {
        if (!memberRequest.getEmail().equals(email)) {
            throw new IllegalArgumentException("Email은 수정할 수 없습니다.");
        }
        memberDao.update(memberRequest.toMember());
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }
    public void deleteMember(String email) {
        memberDao.deleteByEmail(email);
    }
}
