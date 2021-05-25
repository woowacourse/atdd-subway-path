package wooteco.subway.path.dto;

import java.util.List;

public class PathDto {

    private final List<Long> paths;
    private final int distance;

    public PathDto(List<Long> paths, int distance) {
        this.paths = paths;
        this.distance = distance;
    }

    public List<Long> getPaths() {
        return paths;
    }

    public int getDistance() {
        return distance;
    }
}
