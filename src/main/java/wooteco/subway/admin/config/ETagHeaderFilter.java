package wooteco.subway.admin.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

// TODO: ETag 관련 설정하기
public class ETagHeaderFilter {
    @Bean
    public FilterRegistrationBean<ShallowEtagHeaderFilter>
    shallowEtagHeaderFilter() {
        FilterRegistrationBean<ShallowEtagHeaderFilter> filter
                = new FilterRegistrationBean<>( new ShallowEtagHeaderFilter());
        filter.addUrlPatterns("/lines/detail");
        filter.setName("ETag");
        return filter;
    }
}
