package wooteco.subway.auth.ui.dto;

import wooteco.subway.auth.ui.dto.valid.StringValidation;

import java.beans.ConstructorProperties;

public class TokenRequest {

    @StringValidation
    private final String token;

    @ConstructorProperties({"token"})
    public TokenRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
