package wooteco.subway.auth.ui;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.auth.dto.LoginMember;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.dto.MemberResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private AuthService authService;
    private HttpCookie httpCookie;

    public AuthenticationPrincipalArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthenticationPrincipal.class) != null;
    }

    // parameter에 @AuthenticationPrincipal이 붙어있는 경우 동작
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        // TODO: 유효한 로그인인 경우 LoginMember 만들어서 응답하기
        // toke
        return new LoginMember(authService.getEmailByToken(token));
//        for (Cookie cookie : httpServletRequest.getCookies()) {
//            if (cookie.getName().equals("accessToken")) {
//                System.out.println(cookie.getValue());
//            }
//        }
//        throw new IllegalArgumentException("응 쿠키 없어~");
    }
}
