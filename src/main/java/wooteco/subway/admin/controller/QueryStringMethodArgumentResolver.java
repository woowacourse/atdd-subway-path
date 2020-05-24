package wooteco.subway.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class QueryStringMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;

    public QueryStringMethodArgumentResolver(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(QueryString.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
        final String json = queryToJson(request.getQueryString());
        return objectMapper.readValue(json, parameter.getParameterType());
    }

    private String queryToJson(String query) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"");

        for (int i = 0; i < query.length(); i++) {
            stringBuilder.append(queryCharToString(query.charAt(i)));
        }
        stringBuilder.append("\"}");
        return stringBuilder.toString();
    }

    private String queryCharToString(char queryChar) {
        switch (queryChar) {
            case '=':
                return "\":\"";
            case '&':
                return "\",\"";
            default:
                return String.valueOf(queryChar);
        }
    }
}
