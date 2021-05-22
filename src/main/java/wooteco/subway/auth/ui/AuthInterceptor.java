package wooteco.subway.auth.ui;

import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.exception.AuthorizationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        String accessToken = request.getHeader("Authorization");
        if (accessToken == null) {
            throw new AuthorizationException("토큰이 없습니다.");
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
