package wooteco.subway.admin.dto;

public class PathRequest {
    private Long source;
    private Long target;
    private String type;

    public PathRequest() {
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public String getType() {
        return type;
    }
}
