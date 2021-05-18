package wooteco.subway.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.auth.dto.LoginMember;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.dto.MemberCreateRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.dto.MemberUpdateRequest;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    public MemberController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberCreateRequest request) {
        memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/me")).build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(
            @AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse member = MemberResponse.of(loginMember);
        return ResponseEntity.ok(member);
    }

    @PutMapping("/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(
            @AuthenticationPrincipal LoginMember loginMember, @RequestBody @Valid MemberUpdateRequest request) {
        authService.checkPassword(loginMember.toMember(), request.getPassword());
        memberService.updateMember(loginMember.getId(), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(
            @AuthenticationPrincipal LoginMember loginMember) {
        memberService.deleteMember(loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
