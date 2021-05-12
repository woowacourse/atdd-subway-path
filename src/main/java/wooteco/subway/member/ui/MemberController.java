package wooteco.subway.member.ui;

import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

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
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id,
        @RequestBody MemberRequest param) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request);
        MemberResponse memberResponse = memberService.findMemberByToken(token);
        return ResponseEntity.ok(memberResponse);
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(HttpServletRequest request,
        @RequestBody MemberRequest memberRequest) {
        String token = AuthorizationExtractor.extract(request);
        MemberResponse memberResponse = memberService.updateMemberByToken(token, memberRequest);
        return ResponseEntity.ok(memberResponse);
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<Void> deleteMemberOfMine(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request);
        memberService.deleteMemberByToken(token);
        return ResponseEntity.noContent().build();
    }
}
