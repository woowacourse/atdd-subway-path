package wooteco.subway.member.application;

import java.util.Optional;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import wooteco.subway.exception.DuplicateEmailException;
import wooteco.subway.exception.EmailNotFoundException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        try {
            Member member = memberDao.insert(request.toMember());
            return MemberResponse.of(member);
        } catch (DuplicateKeyException e) {
            throw new DuplicateEmailException();
        }
    }

    public MemberResponse findMember(Long id) {
        Member member = memberDao.findById(id).orElseThrow(EmailNotFoundException::new);
        return MemberResponse.of(member);
    }


    public void updateMember(long id, MemberRequest memberRequest) {
        Member member = memberDao.findById(id).orElseThrow(EmailNotFoundException::new);
        Member newMember = new Member(member.getId(), memberRequest.getEmail(), memberRequest.getPassword(),
            memberRequest.getAge());
        memberDao.update(newMember);
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }
}
