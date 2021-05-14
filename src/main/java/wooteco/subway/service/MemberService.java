package wooteco.subway.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.MemberDao;
import wooteco.subway.domain.Member;
import wooteco.subway.dto.MemberRequest;
import wooteco.subway.dto.MemberResponse;

@Service
public class MemberService {
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private MemberDao memberDao;

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
                .orElseThrow(() -> new IllegalArgumentException("이메일을 잘못 입력하셨습니다."));
        String encodedPassword = member.getPassword();
        if (PASSWORD_ENCODER.matches(password, encodedPassword)) {
            return MemberResponse.of(member);
        }
        throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
    }
}
