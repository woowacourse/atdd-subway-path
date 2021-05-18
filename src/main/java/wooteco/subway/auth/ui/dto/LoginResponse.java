package wooteco.subway.auth.ui.dto;

import java.beans.ConstructorProperties;

public class LoginResponse {

    private final String accessToken;

    @ConstructorProperties("accessToken")
    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

}
