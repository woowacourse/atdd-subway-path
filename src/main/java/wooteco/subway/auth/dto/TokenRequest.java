package wooteco.subway.auth.dto;

import java.beans.ConstructorProperties;

public class TokenRequest {

    private final String token;

    @ConstructorProperties({"token"})
    public TokenRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
