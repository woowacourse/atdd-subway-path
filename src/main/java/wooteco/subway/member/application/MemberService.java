package wooteco.subway.member.application;

import org.springframework.stereotype.Service;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberDao.insert(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberDao.findById(id);
        return MemberResponse.of(member);
    }

    public MemberResponse findMemberByToken(String token) {
        validateToken(token);
        String payload = jwtTokenProvider.getPayload(token);
        return MemberResponse.of(findMemberByEmail(payload));
    }

    private void validateToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException();
        }
    }

    private Member findMemberByEmail(String email) {
        Member member = memberDao.findByEmail(email);
        return member;
    }

    public MemberResponse updateMemberByToken(String token, MemberRequest memberRequest) {
        validateToken(token);
        String payload = jwtTokenProvider.getPayload(token);
        Member member = findMemberByEmail(payload);
        updateMember(member.getId(), memberRequest);
        return new MemberResponse(member.getId(), memberRequest.getEmail(), memberRequest.getAge());
    }

    public void updateMember(Long id, MemberRequest memberRequest) {
        memberDao.update(new Member(id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge()));
    }

    public void deleteMember(Long id) {
        memberDao.deleteById(id);
    }

    public void deleteMemberByToken(String token) {
        validateToken(token);
        String payload = jwtTokenProvider.getPayload(token);
        memberDao.deleteByEmail(payload);
    }
}
