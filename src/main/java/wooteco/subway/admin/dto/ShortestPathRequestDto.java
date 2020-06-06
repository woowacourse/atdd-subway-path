package wooteco.subway.admin.dto;

public class ShortestPathRequestDto {
    private String source;
    private String target;

    public ShortestPathRequestDto() {
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}