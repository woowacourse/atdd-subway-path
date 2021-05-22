package wooteco.subway.auth.ui.dto;

import wooteco.subway.auth.ui.dto.valid.StringValidation;

import java.beans.ConstructorProperties;

public class LoginResponse {

    @StringValidation
    private final String accessToken;

    @ConstructorProperties("accessToken")
    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

}
