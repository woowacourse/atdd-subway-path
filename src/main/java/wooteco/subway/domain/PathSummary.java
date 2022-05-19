package wooteco.subway.domain;

public class PathSummary {

    private final Path path;
    private final int fare;

    public PathSummary(Path path, int fare) {
        this.path = path;
        this.fare = fare;
    }

    public Path getPath() {
        return path;
    }

    public int getFare() {
        return fare;
    }
}
