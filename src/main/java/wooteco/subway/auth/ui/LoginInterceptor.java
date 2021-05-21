package wooteco.subway.auth.ui;

import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String accessToken = AuthorizationExtractor.extract(request);
        return jwtTokenProvider.validateToken(accessToken);
    }
}