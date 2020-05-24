package wooteco.subway.admin.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import wooteco.subway.admin.controller.QueryStringMethodArgumentResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final QueryStringMethodArgumentResolver queryStringMethodArgumentResolver;

    public WebMvcConfig(
        QueryStringMethodArgumentResolver queryStringMethodArgumentResolver) {
        this.queryStringMethodArgumentResolver = queryStringMethodArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(queryStringMethodArgumentResolver);
    }
}
