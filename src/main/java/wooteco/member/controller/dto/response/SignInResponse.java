package wooteco.member.controller.dto.response;

public class SignInResponse {
    private String accessToken;

    public SignInResponse() {
    }

    public SignInResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
