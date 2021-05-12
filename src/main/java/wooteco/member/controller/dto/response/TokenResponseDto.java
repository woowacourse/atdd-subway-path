package wooteco.member.controller.dto.response;

public class TokenResponseDto {
    private String accessToken;

    public TokenResponseDto() {
    }

    public TokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
