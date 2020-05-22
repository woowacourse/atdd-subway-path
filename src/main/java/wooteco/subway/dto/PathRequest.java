package wooteco.subway.dto;

import javax.validation.constraints.NotBlank;

public class PathRequest {
    @NotBlank
    private String source;
    @NotBlank
    private String target;
    @NotBlank
    private String type;

    public PathRequest(String source, String target, String type) {
        System.out.println("생성하러 들어왓어!!!");
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getType() {
        return type;
    }
}
