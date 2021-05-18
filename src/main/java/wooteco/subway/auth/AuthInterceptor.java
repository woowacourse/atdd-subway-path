package wooteco.subway.auth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.domain.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = AuthorizationExtractor.extract(request);
        if (StringUtils.isEmpty(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        Member memberByToken = authService.findMemberByToken(token);
        request.setAttribute("id", memberByToken.getId());
        return true;
    }
}
