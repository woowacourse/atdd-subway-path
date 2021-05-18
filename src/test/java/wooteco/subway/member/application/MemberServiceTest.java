package wooteco.subway.member.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.member.application.dto.MemberRequestDto;
import wooteco.subway.member.application.dto.MemberResponseDto;
import wooteco.subway.member.ui.dto.MemberResponse;
import wooteco.subway.member.application.dto.TokenRequestDto;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.exception.InvalidMemberException;
import wooteco.subway.member.infrastructure.dao.MemberDao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "123";
    final int AGE = 10;

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberDao memberDao;

    @DisplayName("새로운 멤버를 생성한다.")
    @Test
    void createMember() {
        given(memberDao.insert(any(Member.class))).willReturn(new Member(1L, EMAIL, PASSWORD, AGE));

        MemberResponseDto member = memberService.createMember(
                new MemberRequestDto(
                        EMAIL,
                        PASSWORD,
                        AGE
                ));

        assertThat(member).
                usingRecursiveComparison()
                .isEqualTo(new MemberResponseDto(
                        1L,
                        EMAIL,
                        AGE
                ));
    }

    @DisplayName("멤버를 찾는다")
    @Test
    void findMember() {
        given(memberDao.findById(1L)).willReturn(new Member(
                1L,
                EMAIL,
                PASSWORD,
                AGE
        ));

        MemberResponseDto member = memberService.findMember(1L);

        assertThat(member)
                .usingRecursiveComparison()
                .isEqualTo(new MemberResponseDto(
                        1L,
                        EMAIL,
                        AGE
                ));
    }

    @DisplayName("인증에 실패하면 예외")
    @Test
    void authenticate() {
        given(memberDao.checkFrom(anyString(), anyString())).willReturn(false);

        assertThatThrownBy(
                () -> memberService.authenticate(
                        new TokenRequestDto(EMAIL, "1")
                )
        ).isInstanceOf(InvalidMemberException.class);
    }

    @DisplayName("이메일로 멤버를 찾는다.")
    @Test
    void findByEmail() {
        given(memberDao.findByEmail(EMAIL)).willReturn(new Member(1L, EMAIL, PASSWORD, AGE));

        MemberResponseDto memberByEmail = memberService.findByEmail(EMAIL);

        assertThat(memberByEmail)
                .usingRecursiveComparison()
                .isEqualTo(new MemberResponseDto(1L, EMAIL, AGE));
    }
}