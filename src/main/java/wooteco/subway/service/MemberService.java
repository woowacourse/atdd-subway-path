package wooteco.subway.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.MemberDao;
import wooteco.subway.domain.Member;
import wooteco.subway.dto.MemberRequest;
import wooteco.subway.dto.MemberResponse;

@Service
public class MemberService {
    private MemberDao memberDao;
    private PasswordEncoder passwordEncoder;

    public MemberService(MemberDao memberDao, PasswordEncoder passwordEncoder) {
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberResponse createMember(MemberRequest request) {
        String email = request.getEmail();
        String password = passwordEncoder.encode(request.getPassword());
        Integer age = request.getAge();
        Member member = memberDao.insert(new Member(email, password, age));
        return MemberResponse.of(member);
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
        Member member = memberDao.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("이메일을 잘못 입력하셨습니다."));
        if (passwordEncoder.matches(password, member.getPassword())) {
            return MemberResponse.of(member);
        }
        throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
    }
}
