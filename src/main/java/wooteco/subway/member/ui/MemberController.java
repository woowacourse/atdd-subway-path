package wooteco.subway.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity createMember(@RequestBody @Valid MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/me")
    public ResponseEntity<LoginMember> findMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(loginMember);
    }

    @PutMapping("/me")
    public ResponseEntity updateMemberOfMine(@AuthenticationPrincipal LoginMember loginMember,
                                             @Valid @RequestBody MemberRequest param) {
        memberService.updateMember(loginMember, param);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        memberService.deleteMember(loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
