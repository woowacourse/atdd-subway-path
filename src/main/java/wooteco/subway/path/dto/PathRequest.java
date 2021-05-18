package wooteco.subway.path.dto;

import javax.validation.constraints.NotNull;

public class PathRequest {
    @NotNull
    private final long source;
    @NotNull
    private final long target;

    public PathRequest(long source, long target) {
        this.source = source;
        this.target = target;
    }

    public long getSource() {
        return source;
    }

    public long getTarget() {
        return target;
    }
}
