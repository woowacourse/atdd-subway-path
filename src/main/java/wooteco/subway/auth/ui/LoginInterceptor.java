package wooteco.subway.auth.ui;

import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.exception.AuthorizationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getHeader("Authorization");
        if (accessToken == null && !request.getMethod().equals("OPTIONS")) {
            throw new AuthorizationException();
        }
        return true;
    }
}
