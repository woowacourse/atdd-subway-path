package wooteco.subway.auth.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthenticationPrincipalArgumentResolverTest {

    @Autowired
    private AuthenticationPrincipalArgumentResolver customResolver;

    @MockBean
    private MethodParameter mockMethodParameter;

    @MockBean
    private ModelAndViewContainer mockModelAndView;

    @MockBean
    private NativeWebRequest mockWebRequest;

    @MockBean
    private WebDataBinderFactory mockBinderFactory;

    @MockBean
    private AuthService mockAuthService;

    @Value("${security.jwt.token.secret-key}")
    private String mockTokenKey;

    @DisplayName("web 요청에 authorization 형식이 올바르지 않은 경우 예외 발생")
    @Test
    void resolveArgumentWithInvalidToken() {
        final String invalidPatternToken = "invalidValue";
        final Member mockMember = new Member(1L, "email", "password", 1);

        Mockito.when(mockWebRequest.getHeader(Mockito.anyString()))
                .thenReturn(invalidPatternToken);

        Mockito.when(mockAuthService.findMemberByToken(Mockito.anyString()))
                .thenReturn(mockMember);

        expectExceptionWithResolveRequest();
    }

    @DisplayName("요청 토큰에 해당하는 유저가 없는 경우 예외 발생")
    @Test
    void resolveArgumentWithNonExistentUserToken() {
        final String validPatternToken = "bearer "+ mockTokenKey;

        Mockito.when(mockWebRequest.getHeader(Mockito.anyString()))
                .thenReturn(validPatternToken);

        Mockito.when(mockAuthService.findMemberByToken(Mockito.anyString()))
                .thenThrow(new IllegalArgumentException());

        expectExceptionWithResolveRequest();
    }

    private void expectExceptionWithResolveRequest() {
        assertThatThrownBy(() -> customResolver.resolveArgument(
                mockMethodParameter
                , mockModelAndView
                , mockWebRequest
                , mockBinderFactory)).isInstanceOf(IllegalArgumentException.class);
    }
}