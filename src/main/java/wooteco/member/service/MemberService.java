package wooteco.member.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        String encryptedPassword = passwordEncoder.encode(memberRequest.getPassword());
        Member memberToInsert = Member.of(memberRequest.getEmail(), encryptedPassword, memberRequest.getAge());
        Member member = memberDao.insert(memberToInsert);
        return MemberResponse.from(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberDao.findById(id);
        return MemberResponse.from(member);
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        String encryptedPassword = passwordEncoder.encode(memberRequest.getPassword());
        memberDao.update(Member.of(id, memberRequest.getEmail(), encryptedPassword, memberRequest.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }
}
