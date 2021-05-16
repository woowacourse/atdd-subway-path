package wooteco.subway.member.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wooteco.subway.auth.exception.AuthException;
import wooteco.subway.auth.exception.AuthExceptionStatus;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class MemberService {
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        String encodedPassword = PASSWORD_ENCODER.encode(request.getPassword());
        Member member = new Member(request.getEmail(), encodedPassword, request.getAge());
        Member savedMember = memberDao.insert(member);
        return MemberResponse.of(savedMember);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberDao.findById(id);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }

    public MemberResponse logIn(String email, String password) {
        Member member = memberDao.findByEmail(email)
                .orElseThrow(() -> new AuthException(AuthExceptionStatus.INVALID_ACCESS));
        String encodedPassword = member.getPassword();
        if (PASSWORD_ENCODER.matches(password, encodedPassword)) {
            return MemberResponse.of(member);
        }
        throw new AuthException(AuthExceptionStatus.INVALID_ACCESS);
    }
}
