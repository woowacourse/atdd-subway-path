package wooteco.subway.auth.ui;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
public class TokenValidatorInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenValidatorInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String token = AuthorizationExtractor.extract(request);
        jwtTokenProvider.validateToken(token);

        return !Objects.isNull(token);
    }
}
