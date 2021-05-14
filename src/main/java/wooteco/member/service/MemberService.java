package wooteco.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wooteco.member.controller.dto.request.MemberRequestDto;
import wooteco.member.controller.dto.response.MemberResponseDto;
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

    public MemberResponseDto createMember(MemberRequestDto memberRequestDto) {
        String encryptedPassword = passwordEncoder.encode(memberRequestDto.getPassword());
        Member memberToInsert = new Member(memberRequestDto.getEmail(), encryptedPassword, memberRequestDto.getAge());
        Member member = memberDao.insert(memberToInsert);
        return MemberResponseDto.of(member);
    }

    public MemberResponseDto findMember(Long id) {
        Member member = memberDao.findById(id);
        return MemberResponseDto.of(member);
    }

    public void updateMember(Long id, MemberRequestDto memberRequestDto) {
        String encryptedPassword = passwordEncoder.encode(memberRequestDto.getPassword());
        memberDao.update(new Member(id, memberRequestDto.getEmail(), encryptedPassword, memberRequestDto.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }
}
