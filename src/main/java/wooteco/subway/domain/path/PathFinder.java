package wooteco.subway.domain.path;

public interface PathFinder {
    PathResult getPath(Long source, Long target, int age);
}
