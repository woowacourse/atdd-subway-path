package wooteco.member.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import wooteco.exception.HttpException;
import wooteco.member.controller.dto.request.MemberRequestDto;
import wooteco.member.controller.dto.response.MemberResponseDto;
import wooteco.member.dao.MemberDao;
import wooteco.member.domain.Member;

@Service
public class MemberService {
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponseDto createMember(MemberRequestDto memberRequestDto) {
        Member member = memberDao.insert(memberRequestDto.toMember());
        return MemberResponseDto.of(member);
    }

    public MemberResponseDto findMember(Long id) {
        Member member = memberDao.findById(id)
            .orElseThrow(() -> new HttpException(HttpStatus.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다."));
        return MemberResponseDto.of(member);
    }

    public void updateMember(Long id, MemberRequestDto memberRequestDto) {
        memberDao.update(new Member(id, memberRequestDto.getEmail(), memberRequestDto.getPassword(), memberRequestDto.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }
}
