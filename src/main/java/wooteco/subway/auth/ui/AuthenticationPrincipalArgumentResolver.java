package wooteco.subway.auth.ui;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberResponse;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;
    private final MemberService memberService;

    public AuthenticationPrincipalArgumentResolver(
        AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    // parameter에 @AuthenticationPrincipal이 붙어있는 경우 동작
    @Override
    public MemberResponse resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        // TODO: 유효한 로그인인 경우 LoginMember 만들어서 응답하기
        String payLoad = AuthorizationExtractor.extract(
            Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)));
        return memberService.findMemberByPayLoad(authService.getPayLoad(payLoad));
    }
}
