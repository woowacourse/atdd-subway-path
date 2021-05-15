package wooteco.subway.auth.ui;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import io.jsonwebtoken.Jwts;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.application.AuthorizationException;
import wooteco.subway.member.domain.LoginMember;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationPrincipalArgumentResolver(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        final String accessToken =
                AuthorizationExtractor.extract(Objects.requireNonNull(
                        webRequest.getNativeRequest(HttpServletRequest.class)));
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new AuthorizationException("유효하지 않은 토큰입니다");
        }

        final Long memberId =  Long.valueOf(jwtTokenProvider.getPayload(accessToken));
        return new LoginMember(memberId);
    }
}
