package wooteco.subway.auth.infrastructure;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class CookieProvider {
    private static final String COOKIE_KEY = "token";
    public ResponseCookie createCookie(String value){
        return ResponseCookie.from(COOKIE_KEY, value)
                .httpOnly(true)
                .sameSite("Lax")
                .path("/")
                .build();
    }

    public Cookie getCookie(HttpServletRequest req, String cookieName){
        final Cookie[] cookies = req.getCookies();
        if(cookies==null) return null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(cookieName))
                return cookie;
        }
        return null;
    }
}
