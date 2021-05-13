package wooteco.subway.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable Long id) {
        MemberResponse member = memberService.findMember(id);
        return ResponseEntity.ok().body(member);
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse memberResponse = memberService.findMember(loginMember.getId());
        return ResponseEntity.ok().body(memberResponse);
    }

    @PutMapping("/members/me")
    public ResponseEntity<Void> updateMemberOfMine(@AuthenticationPrincipal LoginMember loginMember, @RequestBody MemberRequest param) {
        memberService.updateMember(loginMember.getId(), param);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<Void> deleteMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        memberService.deleteMember(loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
