package wooteco.member.controller.config;

import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.member.service.AuthService;
import wooteco.member.domain.AuthenticationPrincipal;
import wooteco.member.controller.dto.request.LoginMember;
import wooteco.member.controller.dto.response.MemberResponse;

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
        String accessToken = webRequest.getHeader("Authorization");
        String splitToken = Objects.requireNonNull(accessToken).split(" ")[1];
        MemberResponse member = authService.findMemberByToken(splitToken);
        return new LoginMember(member.getId());
    }
}
