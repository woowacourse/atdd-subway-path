package wooteco.subway.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.exception.InvalidMemberInformationException;
import wooteco.subway.exception.InvalidTokenException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMemberId;
import wooteco.subway.member.domain.Member;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("test")
public class AuthServiceTest {

    private final String testEmail = "email@email.com";
    private final String testPassword = "password";
    private final Integer age = 20;
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberDao memberDao;

    @DisplayName("유효한 회원은 토큰을 생성한다.")
    @Test
    public void createToken() {
        // given
        memberDao.insert(new Member(testEmail, testPassword, age));
        TokenRequest tokenRequest = new TokenRequest(testEmail, testPassword);

        // when
        String token = authService.createToken(tokenRequest);

        // then
        assertThat(authService.isValidToken(token)).isTrue();
    }

    @DisplayName("유효하지 않은 회원은 토큰을 생성할 수 없다.")
    @Test
    public void createTokenInvalidMemberInformationException1() {
        // given
        TokenRequest tokenRequest = new TokenRequest(testEmail, testPassword);

        // when, then
        assertThatThrownBy(() -> {
            authService.createToken(tokenRequest);
        }).isInstanceOf(InvalidMemberInformationException.class);
    }

    @DisplayName("토큰 검증이 통과하면 true를 반환한다.")
    @Test
    public void validToken() {
        // given
        memberDao.insert(new Member(testEmail, testPassword, age));
        TokenRequest tokenRequest = new TokenRequest(testEmail, testPassword);
        String token = authService.createToken(tokenRequest);

        // when, then
        assertThat(authService.isValidToken(token)).isTrue();
    }

    @DisplayName("토큰 검증이 실패하면 false를 반환한다.")
    @Test
    public void invalidToken() {
        // given
        memberDao.insert(new Member(testEmail, testPassword, age));
        TokenRequest tokenRequest = new TokenRequest(testEmail, testPassword);
        String token = authService.createToken(tokenRequest);

        // when, then
        assertThat(authService.isValidToken(token + "invalid")).isFalse();
    }

    @DisplayName("토큰으로 회원 아이디를 가져온다.")
    @Test
    public void findLoginMemberId() {
        // given
        memberDao.insert(new Member(testEmail, testPassword, age));
        TokenRequest tokenRequest = new TokenRequest(testEmail, testPassword);
        String token = authService.createToken(tokenRequest);

        // when
        LoginMemberId loginMemberId = authService.findLoginMemberId(token);

        // then
        assertThat(loginMemberId.getId()).isEqualTo(1L);
    }

    @DisplayName("유효하지 않은 토큰으로 회원 아이디를 가져올 수 없다.")
    @Test
    public void findLoginMemberIdInvalidMemberInformationException() {
        // given
        memberDao.insert(new Member(testEmail, testPassword, age));
        TokenRequest tokenRequest = new TokenRequest(testEmail, testPassword);
        String token = authService.createToken(tokenRequest);

        // when, then
        assertThatThrownBy(() -> {
            authService.findLoginMemberId(token + "invalid");
        }).isInstanceOf(InvalidTokenException.class);
    }

}
