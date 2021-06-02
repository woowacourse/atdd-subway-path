package wooteco.subway.member.application;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.BusinessRelatedException;
import wooteco.subway.exception.application.ObjectNotFoundException;
import wooteco.subway.member.dao.MemberDao;
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
            return MemberResponse.from(member);
        } catch (DataAccessException e) {
            throw new BusinessRelatedException("멤버 추가에 실패했습니다.");
        }
    }

    public MemberResponse findMember(Long id) {
        return MemberResponse.from(
            memberDao.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("유저를 찾는데 실패했습니다."))
        );
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        try {
            memberDao.update(
                new Member(id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge())
            );
        } catch (DataAccessException e) {
            throw new BusinessRelatedException("멤버 수정에 실패했습니다.");
        }
    }

    public void deleteMember(Long id) {
        try {
            memberDao.deleteById(id);
        } catch (DataAccessException e) {
            throw new BusinessRelatedException("멤버 제거에 실패했습니다.");
        }
    }
}
