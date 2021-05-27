package wooteco.subway.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PathRequest {
    @NotNull
    private final long source;
    @NotNull
    private final long target;

    public PathRequest(long source, long target) {
        this.source = source;
        this.target = target;
    }
}
