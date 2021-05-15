package wooteco.subway.auth.infrastructure;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Objects;

public class AuthorizationExtractor {
    private static final String EMPTY_STRING = "";

    public static String extract(Cookie[] cookies) {
        if (Objects.isNull(cookies)){
            return EMPTY_STRING;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equalsIgnoreCase("accessToken"))
                .map(Cookie::getValue)
                .findAny()
                .orElse(EMPTY_STRING);
    }
}
