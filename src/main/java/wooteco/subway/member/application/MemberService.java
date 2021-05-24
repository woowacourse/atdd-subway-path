package wooteco.subway.member.application;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.exception.UserLoginFailException;
import wooteco.subway.exceptions.SubWayCustomException;
import wooteco.subway.exceptions.SubWayException;
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
        Member member = request.toMember();
        memberValidate(member);
        Member newMember = memberDao.insert(request.toMember());
        return MemberResponse.of(newMember);
    }

    private void memberValidate(Member member) {
        if (memberDao.existMemberOtherThanMeByEmail(member)) {
            throw new SubWayCustomException(SubWayException.DUPLICATE_EMAIL_EXCEPTION);
        }
    }

    public MemberResponse findMember(Long id) {
        Member member = memberDao.findById(id);
        return MemberResponse.of(member);
    }

    public MemberResponse findMemberByEmail(String email) {
        return MemberResponse.of(memberDao.findByEmail(email));
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        Member member = new Member(id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge());
        memberValidate(member);
        memberDao.update(member);
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }

    public MemberResponse findMemberByTokenRequest(TokenRequest tokenRequest) {
        try{
            Member member = memberDao.findByTokenRequest(tokenRequest);
            return MemberResponse.of(member);
        } catch (EmptyResultDataAccessException exception){
            throw new UserLoginFailException();
        }

    }
}
