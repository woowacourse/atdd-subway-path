package wooteco.subway.path.dto;

public class PathIdModel {
    private long source;
    private long target;

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
