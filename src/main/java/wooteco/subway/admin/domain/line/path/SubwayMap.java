package wooteco.subway.admin.domain.line.path;

public interface SubwayMap {
    Path findShortestPath(Long departureId, Long arrivalId);
}
