package wooteco.subway.web.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.domain.member.AuthenticationPrincipal;
import wooteco.subway.domain.member.Member;
import wooteco.subway.web.member.dto.MemberRequest;
import wooteco.subway.web.member.dto.MemberResponse;
import wooteco.subway.service.member.MemberService;

import java.net.URI;

@RestController
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
    public ResponseEntity<Void> updateMember(@PathVariable Long id, @RequestBody MemberRequest param) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal Member member) {
        final MemberResponse memberResponse = memberService.findMemberByEmail(member.getEmail());
        return ResponseEntity.ok(memberResponse);
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(
            @AuthenticationPrincipal Member member, @RequestBody MemberRequest memberRequest) {
        memberService.updateMember(member.getId(), memberRequest);
        final MemberResponse memberResponse = memberService.findMember(member.getId());
        return ResponseEntity.ok(memberResponse);
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<Void> deleteMemberOfMine(@AuthenticationPrincipal Member member) {
        memberService.deleteMember(member.getId());
        return ResponseEntity.noContent().build();
    }
}
