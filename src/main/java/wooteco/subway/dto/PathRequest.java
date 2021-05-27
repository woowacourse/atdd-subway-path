package wooteco.subway.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PathRequest {
    @NotNull
    private final Long source;
    @NotNull
    private final Long target;

    public PathRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }
}
