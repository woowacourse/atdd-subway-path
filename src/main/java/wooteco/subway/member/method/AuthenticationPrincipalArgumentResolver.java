package wooteco.subway.member.method;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.member.domain.AuthenticationPrincipal;
import wooteco.subway.member.infra.AuthorizationExtractor;
import wooteco.subway.member.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private AuthService authService;

    public AuthenticationPrincipalArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        final HttpServletRequest request = Objects.requireNonNull(
                webRequest.getNativeRequest(HttpServletRequest.class));
        final String accessToken = AuthorizationExtractor.extract(request);
        return authService.findMemberByToken(accessToken);
    }
}
