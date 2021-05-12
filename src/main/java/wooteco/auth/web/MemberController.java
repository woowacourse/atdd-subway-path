package wooteco.auth.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.auth.domain.AuthenticationPrincipal;
import wooteco.auth.domain.Member;
import wooteco.auth.service.MemberService;
import wooteco.auth.web.dto.MemberRequest;
import wooteco.auth.web.dto.MemberResponse;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
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
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody MemberRequest param) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(MemberResponse.of(member));
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
