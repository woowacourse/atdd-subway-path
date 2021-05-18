package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.member.application.dto.MemberRequestDto;
import wooteco.subway.member.application.dto.MemberResponseDto;
import wooteco.subway.member.ui.dto.MemberResponse;
import wooteco.subway.member.application.dto.TokenRequestDto;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.exception.InvalidMemberException;
import wooteco.subway.member.infrastructure.dao.MemberDao;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponseDto createMember(MemberRequestDto memberRequestDto) {
        Member member = memberDao.insert(
                new Member(
                        memberRequestDto.getEmail(),
                        memberRequestDto.getPassword(),
                        memberRequestDto.getAge()
                )
        );

        return MemberResponseDto.of(member);
    }

    public MemberResponseDto findMember(Long id) {
        Member member = memberDao.findById(id);

        return MemberResponseDto.of(member);
    }

    @Transactional
    public void updateMember(String email, MemberRequestDto memberRequestDto) {
        Member originMember = memberDao.findByEmail(email);

        memberDao.update(
                new Member(
                        originMember.getId(),
                        memberRequestDto.getEmail(),
                        memberRequestDto.getPassword(),
                        memberRequestDto.getAge()
                )
        );
    }

    public void deleteMember(String email) {
        memberDao.deleteByEmail(email);
    }

    public void authenticate(TokenRequestDto tokenRequestDto) {
        boolean isValid = memberDao.checkFrom(
                tokenRequestDto.getEmail(),
                tokenRequestDto.getPassword()
        );

        if (!isValid) {
            throw new InvalidMemberException();
        }
    }

    public MemberResponseDto findByEmail(String email) {
        Member member = memberDao.findByEmail(email);

        return new MemberResponseDto(member.getId(), member.getEmail(), member.getAge());
    }

}
