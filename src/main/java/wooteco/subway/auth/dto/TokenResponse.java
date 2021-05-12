package wooteco.subway.auth.dto;

import wooteco.subway.auth.domain.Token;

public class TokenResponse {
    private String accessToken;

    public TokenResponse() {
    }

    public TokenResponse(Token token) {
        this.accessToken = token.accessToken();
    }

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
