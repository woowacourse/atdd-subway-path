package wooteco.subway.domain.path;

import java.util.List;
import java.util.Set;

public class Path {

    private final List<Long> path;
    private final int distance;
    private final Set<Long> usedLineIds;

    public Path(List<Long> path, int distance, Set<Long> usedLineIds) {
        this.path = path;
        this.distance = distance;
        this.usedLineIds = usedLineIds;
    }

    public List<Long> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }

    public Set<Long> getUsedLineIds() {
        return usedLineIds;
    }
}
