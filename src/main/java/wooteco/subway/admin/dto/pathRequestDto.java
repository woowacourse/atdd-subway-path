package wooteco.subway.admin.dto;

public class pathRequestDto {
    private String source;
    private String target;

    public pathRequestDto() {
    }

    public pathRequestDto(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
