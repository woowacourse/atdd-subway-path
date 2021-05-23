package wooteco.subway.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.application.AuthService;
import wooteco.subway.application.MemberService;
import wooteco.subway.domain.member.AuthenticationPrincipal;
import wooteco.subway.presentation.dto.request.LoginMember;
import wooteco.subway.presentation.dto.request.MemberRequest;
import wooteco.subway.presentation.dto.response.MemberResponse;

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
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberRequest request) {
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
            @AuthenticationPrincipal LoginMember loginMember, @RequestBody @Valid MemberRequest request) {
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
