package wooteco.subway.auth.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.auth.application.exception.AuthorizationException;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DisplayName("인증 비즈니스 로직 테스트")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private MemberDao memberDao;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("토큰 생성")
    void createToken() {
        // given
        given(memberDao.findByEmail("air.junseo@gmail.com"))
                .willReturn(Optional.of(new Member(1L, "air.junseo@gmail.com", "1234", 26)));
        given(jwtTokenProvider.createToken("1")).willReturn("access token");

        // when
        TokenResponse response =
                authService.createToken(new TokenRequest("air.junseo@gmail.com", "1234"));

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(new TokenResponse("access token"));
    }

    @Test
    @DisplayName("토큰 생성 - 없는 이메일로 로그인하는 경우")
    void createTokenEmailAuthorizationException() {
        // given
        given(memberDao.findByEmail("air.junseo@gmail.com"))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.createToken(new TokenRequest("air.junseo@gmail.com", "1234")))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("토큰 생성 - 유효하지 않은 비밀번호로 로그인하는 경우")
    void createTokenPasswordAuthorizationException() {
        // given
        given(memberDao.findByEmail("air.junseo@gmail.com"))
                .willReturn(Optional.of(new Member(1L, "air.junseo@gmail.com", "1234", 26)));

        // when & then
        assertThatThrownBy(() -> authService.createToken(new TokenRequest("air.junseo@gmail.com", "12345")))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("payload 얻어오기")
    void getPayload() {
        // given
        given(jwtTokenProvider.getPayload("access token")).willReturn("payload");

        // when
        String payload = authService.getPayload("access token");

        // then
        assertThat(payload).isEqualTo("payload");
    }
}