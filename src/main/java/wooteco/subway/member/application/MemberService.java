package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import java.util.Optional;

@Service
public class MemberService {
    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest request) {
        Optional<Member> optionalMember = memberDao.findByEmail(request.getEmail());

        if(optionalMember.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 Email 입니다.");
        }

        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public Optional<Member> findMember(Long id) {
        return memberDao.findById(id);
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }
}
