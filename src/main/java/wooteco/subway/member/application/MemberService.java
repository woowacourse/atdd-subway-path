package wooteco.subway.member.application;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import wooteco.subway.exception.DuplicateEmailException;
import wooteco.subway.exception.MemberNotFoundException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberDao memberDao;

    public MemberService(PasswordEncoder passwordEncoder,
        MemberDao memberDao) {
        this.passwordEncoder = passwordEncoder;
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        try {
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            Member newMember = new Member(request.getEmail(), encodedPassword, request.getAge());
            Member member = memberDao.insert(newMember);
            return MemberResponse.of(member);
        } catch (DuplicateKeyException e) {
            throw new DuplicateEmailException(request.getEmail());
        }
    }

    public MemberResponse findMember(Long id) {
        Member member = memberDao.findById(id).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(long id, MemberRequest memberRequest) {
        Member member = memberDao.findById(id).orElseThrow(MemberNotFoundException::new);
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        Member newMember = new Member(member.getId(), memberRequest.getEmail(), encodedPassword,
            memberRequest.getAge());
        memberDao.update(newMember);
    }

    public void deleteMember(Long id) {
        try {
            memberDao.deleteById(id);
        } catch (DataAccessException e) {
            throw new MemberNotFoundException();
        }
    }
}
