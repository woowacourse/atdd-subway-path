package wooteco.subway.member.ui;

import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.exception.UnauthorizedException;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@RestController
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider tokenProvider;

    public MemberController(MemberService memberService, JwtTokenProvider tokenProvider) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable Long id) {
        MemberResponse member = memberService.findMember(id);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id,
            @RequestBody MemberRequest param) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(HttpServletRequest request) {
        final String accessToken = AuthorizationExtractor.extract(request);
        if (!tokenProvider.validateToken(accessToken)) {
            throw new UnauthorizedException("유효하지 않은 토큰이얌");
        }
        String email = tokenProvider.getPayload(accessToken);
        return ResponseEntity.ok(memberService.findMemberByEmail(email));
    }

    // TODO: 구현 하기
    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine() {
        return ResponseEntity.ok().build();
    }

    // TODO: 구현 하기
    @DeleteMapping("/members/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine() {
        return ResponseEntity.noContent().build();
    }
}
