package wooteco.subway.auth.ui;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static wooteco.subway.auth.infrastructure.AuthorizationExtractor.BEARER_TYPE;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        String accessToken = request.getHeader("Authorization");

        if ((accessToken == null || accessToken.length() == BEARER_TYPE.length()) && !request.getMethod().equals("OPTIONS")) {
            response.setStatus(401);
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
