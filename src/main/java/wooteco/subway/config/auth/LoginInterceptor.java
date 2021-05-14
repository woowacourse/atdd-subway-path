package wooteco.subway.config.auth;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.config.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class LoginInterceptor implements HandlerInterceptor {
    private static final String AC_REQUEST_METHOD = "Access-Control-Request-Method";
    private static final String AC_REQUEST_HEADERS = "Access-Control-Request-Headers";
    private static final String AC_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private static final String AC_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String AC_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private static final String ORIGIN = "Origin";

    private final AuthService authService;

    public LoginInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isPreflighted(request)) {
            response.setHeader(AC_ALLOW_ORIGIN, request.getHeader(ORIGIN));
            response.setHeader(AC_ALLOW_HEADERS, request.getHeader(AC_REQUEST_HEADERS));
            response.setHeader(AC_ALLOW_METHODS, request.getHeader(AC_REQUEST_METHOD));
            return true;
        }
        String token = AuthorizationExtractor.extract(request);
        authService.validate(token);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private boolean isPreflighted(HttpServletRequest request) {
        String optionMethod = HttpMethod.OPTIONS.toString();
        String currentMethod = request.getMethod();
        String requestHeaders = request.getHeader(AC_REQUEST_HEADERS);
        String requestMethods = request.getHeader(AC_REQUEST_METHOD);
        return optionMethod.equals(currentMethod)
                && isNotNullNorEmpty(requestHeaders)
                && isNotNullNorEmpty(requestMethods);
    }

    private boolean isNotNullNorEmpty(String requestInput) {
        return !Objects.isNull(requestInput) && !requestInput.isEmpty();
    }
}
