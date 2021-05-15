package wooteco.member.controller.dto.response;

public class SignInResponseDto {
    private String accessToken;

    public SignInResponseDto() {
    }

    public SignInResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
