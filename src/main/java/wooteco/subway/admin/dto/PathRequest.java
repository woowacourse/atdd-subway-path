package wooteco.subway.admin.dto;

import javax.validation.constraints.NotBlank;

public class PathRequest {

    @NotBlank(message = "이전 역 이름은 필수 입력 요소입니다.")
    private String sourceName;
    @NotBlank(message = "대상 역 이름은 필수 입력 요소입니다.")
    private String targetName;
    private String type;

    public PathRequest() {
    }

    public PathRequest(
        @NotBlank(message = "이전 역 이름은 필수 입력 요소입니다.") String sourceName,
        @NotBlank(message = "대상 역 이름은 필수 입력 요소입니다.") String targetName, String type) {
        this.sourceName = sourceName;
        this.targetName = targetName;
        this.type = type;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getTargetName() {
        return targetName;
    }

    public String getType() {
        return type;
    }
}
