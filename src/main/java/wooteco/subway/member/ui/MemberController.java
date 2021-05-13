package wooteco.subway.member.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.AuthenticationPrincipal;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.TokenRequest;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.exception.InvalidMemberException;
import wooteco.subway.member.exception.InvalidTokenException;
import wooteco.subway.member.infrastructure.AuthorizationExtractor;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody MemberRequest param) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    // TODO: 구현 하기
    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        String email = loginMember.getEmail();
        Member member = memberService.findByEmail(email);

        return ResponseEntity.ok().body(
                new MemberResponse(member)
        );
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

    @PostMapping("/members/authentication")
    public ResponseEntity<Void> memberAuthenticate(@RequestBody TokenRequest tokenRequest) {
        memberService.authenticate(tokenRequest);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(InvalidMemberException.class)
    private ResponseEntity<Void> handlerInvalidMemberException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Void> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(InvalidTokenException.class)
    private ResponseEntity<Void> handleInvalidTokenException(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
