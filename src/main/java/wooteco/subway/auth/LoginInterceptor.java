package wooteco.subway.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.exception.HttpException;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private final AuthService authService;

    public LoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String accessToken = request.getHeader("Authorization");
            authService.checkValidation(accessToken);
        } catch (Exception e) {
            throw new HttpException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
        return super.preHandle(request, response, handler);
    }
}
