package wooteco.subway.member.application;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.badrequest.DuplicateEmailException;
import wooteco.subway.exception.notfound.MemberNotFoundException;
import wooteco.subway.exception.badrequest.NoRowHasBeenModifiedException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class MemberService {
    private final MemberDao memberDao;

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
        Member member = memberDao.findById(id).orElseThrow(MemberNotFoundException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(LoginMember loginMember, MemberRequest memberRequest) {
        try {
            Member newMember = new Member(loginMember.getId(), memberRequest.getEmail(), memberRequest.getPassword(),
                    memberRequest.getAge());
            memberDao.update(newMember);
        } catch (DuplicateKeyException e) {
            throw new DuplicateEmailException();
        } catch (NoRowHasBeenModifiedException e) {
            throw new MemberNotFoundException(e.getCause());
        }
    }

    public void deleteMember(Long id) {
        try {
            memberDao.deleteById(id);
        } catch (NoRowHasBeenModifiedException e) {
            throw new MemberNotFoundException(e.getCause());
        }
    }
}
