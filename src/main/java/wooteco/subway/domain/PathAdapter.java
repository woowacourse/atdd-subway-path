package wooteco.subway.domain;

public class PathAdapter implements Shortest{
    private final ShortestPath shortestPath;

    public PathAdapter(final ShortestPath shortestPath) {
        this.shortestPath = shortestPath;
    }

    @Override
    public Path getShortestPath(final Station source, final Station target, final Fare fare, final int age) {
        return shortestPath.getPath(source, target, fare, age);
    }

    @Override
    public Long getExpensiveLineId(final Station source, final Station target) {
        return shortestPath.getExpensiveLineId(source, target);
    }
}
