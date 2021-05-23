package wooteco.subway.auth.ui;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;

@Configuration
public class LoginInterceptorConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginInterceptorConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(createLoginInterceptor())
                .addPathPatterns("/api/stations/**")
                .addPathPatterns("/api/lines/**")
                .addPathPatterns("/api/paths/**");
    }

    @Bean
    public HandlerInterceptor createLoginInterceptor() {
        return new LoginInterceptor(jwtTokenProvider);
    }
}
