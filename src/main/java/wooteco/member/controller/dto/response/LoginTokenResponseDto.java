package wooteco.member.controller.dto.response;

public class LoginTokenResponseDto {
    private String accessToken;

    public LoginTokenResponseDto() {
    }

    public LoginTokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
