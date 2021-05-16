package wooteco.auth.domain;

public class Token {

    private final String accessToken;

    public Token(String accessToken) {
        this.accessToken = accessToken;
    }

    public String accessToken() {
        return accessToken;
    }
}
