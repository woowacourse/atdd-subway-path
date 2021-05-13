package wooteco.subway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.service.AuthService;

import java.util.List;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {
    private final AuthService authService;

    public AuthenticationPrincipalConfig(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(createAuthenticationPrincipalArgumentResolver());
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver createAuthenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(authService);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        todo 인터셉트할 url과 제외할 url 등록
//        registry.addInterceptor(new LoginInterceptor())
//                .addPathPatterns("/*")
//                .excludePathPatterns("/login/token");

//        todo LoginInterceptor(extends HandlerInterceptorAdapter).class 구현
//        boolean preHandle(...)을 재정의하여 토큰 값을 검증한다.
//        추가적으로 원하는 검증 로직(토큰 유효성 확인)을 작성하고, 부모 클래스의 preHandle()을 반환한다.
    }
}
