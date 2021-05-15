package wooteco.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.member.controller.dto.request.ApprovedMemberRequest;
import wooteco.member.controller.dto.request.MemberRequest;
import wooteco.member.controller.dto.response.MemberResponse;
import wooteco.member.domain.AuthenticationPrincipal;
import wooteco.member.domain.Member;
import wooteco.member.service.MemberService;

import java.net.URI;

@RequestMapping("/api/members")
@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest memberRequest) {
        MemberResponse memberResponse = memberService.createMember(memberRequest);
        return ResponseEntity.created(URI.create("/api/members/" + memberResponse.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable Long id) {
        MemberResponse memberResponse = memberService.findMember(id);
        return ResponseEntity.ok(memberResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMember(@PathVariable Long id, @RequestBody MemberRequest memberRequest) {
        memberService.updateMember(id, memberRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal ApprovedMemberRequest approvedMemberRequest) {
        return ResponseEntity.ok(MemberResponse.from(approvedMemberRequest));
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateMemberOfMine(@AuthenticationPrincipal ApprovedMemberRequest approvedMemberRequest,
                                                   @RequestBody MemberRequest memberRequest) {
        memberService.updateMember(approvedMemberRequest.getId(), memberRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMemberOfMine(@AuthenticationPrincipal ApprovedMemberRequest approvedMemberRequest) {
        memberService.deleteMember(approvedMemberRequest.getId());
        return ResponseEntity.noContent().build();
    }
}
