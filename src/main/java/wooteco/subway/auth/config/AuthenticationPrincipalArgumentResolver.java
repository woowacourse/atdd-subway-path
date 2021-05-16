package wooteco.subway.auth.config;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.dto.MemberResponse;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private MemberService memberService;

    public AuthenticationPrincipalArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = AuthorizationExtractor.extract(
            Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)));
        String email = memberService.getPayLoad(token);
        MemberResponse memberResponse = memberService.findMemberByEmail(email);
        return memberResponse;
    }
}
