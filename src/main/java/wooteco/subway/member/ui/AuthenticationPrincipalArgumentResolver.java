package wooteco.subway.member.ui;

import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.member.domain.AuthenticationPrincipal;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.exception.InvalidTokenException;
import wooteco.subway.member.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.infrastructure.TokenAuthentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    public static final String TOKEN_EXTRACT_EXCEPTION_MESSAGE = "인증 관련 오류가 발생했습니다.";
    public static final String DELIMITER = "\\.";

    private final TokenAuthentication tokenAuthentication;

    public AuthenticationPrincipalArgumentResolver(TokenAuthentication tokenAuthentication) {
        this.tokenAuthentication = tokenAuthentication;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Objects.requireNonNull(nativeRequest, TOKEN_EXTRACT_EXCEPTION_MESSAGE);

        String token = AuthorizationExtractor.extract(nativeRequest);
        validateToken(token);

        String email = getEmailFromPayload(token);

        return new LoginMember(email);
    }

    private String getEmailFromPayload(String token) {
        String[] chunks = token.split(DELIMITER);
        String payload = new String(Base64.getDecoder().decode(chunks[1]));

        JsonParser jsonParser = new JacksonJsonParser();
        Map<String, Object> stringObjectMap = jsonParser.parseMap(payload);

        return (String) stringObjectMap.get("sub");
    }

    private void validateToken(String token) {
        if (!tokenAuthentication.validate(token)) {
            throw new InvalidTokenException();
        }
    }

}
