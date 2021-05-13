package wooteco.member.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.member.controller.dto.request.MemberRequestDto;
import wooteco.member.controller.dto.response.MemberResponseDto;
import wooteco.member.domain.AuthenticationPrincipal;
import wooteco.member.domain.Member;
import wooteco.member.service.MemberService;

@RequestMapping("/api/members")
@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = memberService.createMember(memberRequestDto);
        return ResponseEntity.created(URI.create("/api/members/" + memberResponseDto.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> findMember(@PathVariable Long id) {
        MemberResponseDto memberResponseDto = memberService.findMember(id);
        return ResponseEntity.ok(memberResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMember(@PathVariable Long id, @RequestBody MemberRequestDto memberRequestDto) {
        memberService.updateMember(id, memberRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> findMemberOfMine(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(new MemberResponseDto(member));
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateMemberOfMine(@AuthenticationPrincipal Member member, @RequestBody MemberRequestDto memberRequestDto) {
        memberService.updateMember(member.getId(), memberRequestDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMemberOfMine(@AuthenticationPrincipal Member member) {
        memberService.deleteMember(member.getId());
        return ResponseEntity.noContent().build();
    }
}
