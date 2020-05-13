package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.PathType;

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