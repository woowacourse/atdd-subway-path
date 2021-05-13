package wooteco.subway.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RequestMapping("/api/members")
@RestController
public class MemberController {
    private MemberService memberService;
    private AuthService authService;


    public MemberController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
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
    public ResponseEntity<Void> updateMember(@PathVariable Long id, @RequestBody MemberRequest param) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request);
        MemberResponse memberResponse = authService.findMemberByToken(token);
        return ResponseEntity.ok().body(memberResponse);
    }

    @PutMapping("/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(HttpServletRequest request, @RequestBody MemberRequest memberRequest) {
        String token = AuthorizationExtractor.extract(request);
        MemberResponse memberResponse = authService.findMemberByToken(token);
        memberService.updateMember(memberResponse.getId(), memberRequest);
        MemberResponse updatedMember = memberService.findMember(memberResponse.getId());
        return ResponseEntity.ok().body(updatedMember);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMemberOfMine(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request);
        MemberResponse memberResponse = authService.findMemberByToken(token);
        memberService.deleteMember(memberResponse.getId());
        return ResponseEntity.noContent().build();
    }
}
