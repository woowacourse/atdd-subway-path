package wooteco.subway.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable Long id) {
        MemberResponse member = memberService.findMember(id);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody MemberRequest param) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/me")
//    public ResponseEntity<MemberResponse> findMemberOfMine(HttpServletRequest request) {
//        String token = AuthorizationExtractor.extract(request);
//        MemberResponse memberResponse = memberService.findMemberByEmail(token);
//        return ResponseEntity.ok().body(memberResponse);
//    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse member = memberService.findMember(loginMember);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/me/{id}")
    public ResponseEntity<MemberResponse> updateMemberOfMine(@PathVariable Long id,
                                                             @Valid @RequestBody MemberRequest memberRequest, HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request);
        memberService.updateMember(id, memberRequest, token);
        MemberResponse memberResponse = memberService.findMemberByEmail(token);
        return ResponseEntity.ok().body(memberResponse);
    }

    @DeleteMapping("/me/{id}")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(@PathVariable Long id, HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request);
        memberService.deleteMember(id, token);
        return ResponseEntity.noContent().build();
    }
}
