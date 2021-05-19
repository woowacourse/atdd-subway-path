package wooteco.subway.path.dto;

import com.sun.istack.internal.NotNull;

public class PathIdModel {
    @NotNull private long source;
    @NotNull private long target;

    public PathIdModel() {
    }

    public long getSource() {
        return source;
    }

    public long getTarget() {
        return target;
    }

    public void setSource(long source) {
        this.source = source;
    }

    public void setTarget(long target) {
        this.target = target;
    }
}
