package wooteco.auth.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.auth.infrastructure.AuthorizationExtractor;
import wooteco.auth.infrastructure.JwtTokenProvider;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        if (!request.getMethod().equalsIgnoreCase(HttpMethod.POST.toString())) {
            String token = AuthorizationExtractor.extract(request);
            jwtTokenProvider.validateToken(token);
        }
        return true;
    }
}
