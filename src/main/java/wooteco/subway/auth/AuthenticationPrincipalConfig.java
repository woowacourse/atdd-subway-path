package wooteco.subway.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.ui.AuthenticationPrincipalArgumentResolver;
import wooteco.subway.auth.ui.TokenValidatorInterceptor;

import java.util.List;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {

    private final AuthService authService;
    private final TokenValidatorInterceptor tokenValidatorInterceptor;

    public AuthenticationPrincipalConfig(AuthService authService, TokenValidatorInterceptor tokenValidatorInterceptor) {
        this.authService = authService;
        this.tokenValidatorInterceptor = tokenValidatorInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenValidatorInterceptor)
                .addPathPatterns("/members/me/**");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        final AuthenticationPrincipalArgumentResolver resolver = new AuthenticationPrincipalArgumentResolver(authService);
        argumentResolvers.add(resolver);
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver createAuthenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(authService);
    }
}
