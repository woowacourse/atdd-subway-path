package wooteco.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wooteco.member.controller.dto.request.MemberRequest;
import wooteco.member.controller.dto.response.MemberResponse;
import wooteco.member.dao.MemberDao;
import wooteco.member.domain.Member;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberDao memberDao, PasswordEncoder passwordEncoder) {
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        String encryptedPassword = passwordEncoder.encode(memberRequest.getPassword());
        Member memberToInsert = new Member(memberRequest.getEmail(), encryptedPassword, memberRequest.getAge());
        Member member = memberDao.insert(memberToInsert);
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberDao.findById(id);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        String encryptedPassword = passwordEncoder.encode(memberRequest.getPassword());
        memberDao.update(new Member(id, memberRequest.getEmail(), encryptedPassword, memberRequest.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }
}
