package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.exception.JwtLoginEmailException;
import wooteco.subway.auth.exception.JwtLoginPasswordException;
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

    public MemberResponse findMemberByEmail(String email) {
        Optional<Member> member = memberDao.findByEmail(email);
        if (member.isPresent()) {
            return MemberResponse.of(member.get());
        }
        throw new JwtLoginEmailException();
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }

    public Member findMemberByEmail(TokenRequest request) {
        Member member = memberDao.findByEmail(request.getEmail()).orElseThrow(JwtLoginEmailException::new);
        if(!member.samePassword(request.getPassword())) {
            throw new JwtLoginPasswordException();
        }
        return member;
    }

    private Member findByEmail(String email) {
        return memberDao.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("없는 이메일임!"));
    }
}
