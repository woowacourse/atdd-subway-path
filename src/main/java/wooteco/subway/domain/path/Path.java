package wooteco.subway.domain.path;

import wooteco.subway.domain.path.factory.PathFactory;
import wooteco.subway.domain.station.Station;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Path {
    private final List<PathEdge> pathEdges;
    private final List<Station> stations;

    public Path(List<PathEdge> pathEdges, List<Station> stations) {
        this.pathEdges = pathEdges;
        this.stations = stations;
    }

    public static Path of(PathFactory pathFactory, Station source, Station target) {
        return pathFactory.createShortestPath(source, target);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int calculateDistance() {
        return (int) pathEdges.stream()
                .mapToDouble(PathEdge::getWeight)
                .sum();
    }

    public int getPathExtraFare(Map<Long, Integer> lineExtraFares) {
        return pathEdges.stream()
                .map(PathEdge::getLineId)
                .mapToInt(lineExtraFares::get)
                .max()
                .orElseThrow(() -> new NoSuchElementException("경로가 존재하지 않습니다."));
    }
}
