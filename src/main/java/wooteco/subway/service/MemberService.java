package wooteco.subway.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.MemberDao;
import wooteco.subway.domain.Member;
import wooteco.subway.dto.MemberRequest;
import wooteco.subway.dto.MemberResponse;
import wooteco.subway.exception.AuthenticationException;

@Service
public class MemberService {
    private MemberDao memberDao;
    private PasswordEncoder passwordEncoder;

    public MemberService(MemberDao memberDao, PasswordEncoder passwordEncoder) {
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
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

    @Transactional
    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), passwordEncoder.encode(memberRequest.getPassword()), memberRequest.getAge()));
    }

    @Transactional
    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }

    public MemberResponse logIn(String email, String password) {
        Member member = memberDao.findByEmail(email).orElseThrow(() -> new AuthenticationException("로그인 정보가 맞지 않습니다."));
        if (passwordEncoder.matches(password, member.getPassword())) {
            return MemberResponse.of(member);
        }
        throw new AuthenticationException("로그인 정보가 맞지 않습니다.");
    }
}
