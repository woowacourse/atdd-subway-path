package wooteco.subway.member.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.application.dto.MemberRequestDto;
import wooteco.subway.member.application.dto.MemberResponseDto;
import wooteco.subway.member.application.dto.TokenRequestDto;
import wooteco.subway.member.domain.AuthenticationPrincipal;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.ui.dto.MemberRequest;
import wooteco.subway.member.ui.dto.MemberResponse;
import wooteco.subway.member.ui.dto.TokenRequest;

import java.net.URI;

@RequestMapping("/members")
@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest request) {
        MemberResponseDto memberResponseDto = memberService.createMember(
                new MemberRequestDto(
                        request.getEmail(),
                        request.getPassword(),
                        request.getAge()
                )
        );

        return ResponseEntity
                .created(URI.create("/members/" + memberResponseDto.getId()))
                .build();
    }

    @GetMapping("{id}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable Long id) {
        MemberResponseDto memberResponseDto = memberService.findMember(id);

        return ResponseEntity.ok().body(
                new MemberResponse(
                        memberResponseDto.getId(),
                        memberResponseDto.getEmail(),
                        memberResponseDto.getAge()
                )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        String email = loginMember.getEmail();
        MemberResponseDto memberResponseDto = memberService.findByEmail(email);

        return ResponseEntity.ok().body(
                new MemberResponse(
                        memberResponseDto.getId(),
                        memberResponseDto.getEmail(),
                        memberResponseDto.getAge()
                )
        );
    }

    @PutMapping("/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(@AuthenticationPrincipal LoginMember loginMember,
                                                             @RequestBody MemberRequest memberRequest) {
        String email = loginMember.getEmail();
        memberService.updateMember(
                email, new MemberRequestDto(
                        memberRequest.getEmail(),
                        memberRequest.getPassword(),
                        memberRequest.getAge()
                )
        );

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        memberService.deleteMember(loginMember.getEmail());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/authentication")
    public ResponseEntity<Void> memberAuthenticate(@RequestBody TokenRequest tokenRequest) {
        memberService.authenticate(
                new TokenRequestDto(
                        tokenRequest.getEmail(),
                        tokenRequest.getPassword()
                )
        );

        return ResponseEntity.ok().build();
    }

}
