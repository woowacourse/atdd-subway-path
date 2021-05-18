package wooteco.subway.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity createMember(@RequestBody MemberRequest request) {
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
        return ResponseEntity.ok(memberService.updateMember(id, param));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("id");
        return ResponseEntity.ok(memberService.findMember(id));
    }

    @PutMapping("/me")
    public ResponseEntity<MemberResponse> updateMemberOfMin(
            HttpServletRequest request, @RequestBody MemberRequest memberRequest) {
        Long id = (Long) request.getAttribute("id");
        return ResponseEntity.ok(memberService.updateMember(id, memberRequest));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMemberOfMine(HttpServletRequest request) {
        Long id = (Long) request.getAttribute("id");
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
