package wooteco.subway.member.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.member.dto.TokenRequest;
import wooteco.subway.member.exception.InvalidMemberException;

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

        MemberResponse member = memberService.createMember(
                new MemberRequest(
                        EMAIL,
                        PASSWORD,
                        AGE
                ));

        assertThat(member).
                usingRecursiveComparison()
                .isEqualTo(new MemberResponse(
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

        MemberResponse member = memberService.findMember(1L);

        assertThat(member)
                .usingRecursiveComparison()
                .isEqualTo(new MemberResponse(
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
                        new TokenRequest(EMAIL, "1")
                )
        ).isInstanceOf(InvalidMemberException.class);
    }

    @DisplayName("이메일로 멤버를 찾는다.")
    @Test
    void findByEmail() {
        given(memberDao.findByEmail(EMAIL)).willReturn(new Member(1L, EMAIL, PASSWORD, AGE));

        Member memberByEmail = memberService.findByEmail(EMAIL);

        assertThat(memberByEmail)
                .usingRecursiveComparison()
                .isEqualTo(new Member(1L, EMAIL, PASSWORD, AGE));
    }
}