package wooteco.subway.path.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import wooteco.subway.exception.application.ValidationFailureException;
import wooteco.subway.station.domain.Station;

public class Path {

    private final List<Station> stations;
    private final List<PathEdge> pathEdges;

    public Path(List<Station> stations, List<PathEdge> pathEdges) {
        validateNullOrEmpty(stations, pathEdges);

        this.stations = stations;
        this.pathEdges = new ArrayList<>(pathEdges);
    }

    private void validateNullOrEmpty(List<Station> stations, List<PathEdge> pathEdges) {
        if (Objects.isNull(stations) || stations.isEmpty()) {
            throw new ValidationFailureException("Path 생성에 실패했습니다. (비어있는 역)");
        }
        if (Objects.isNull(pathEdges) || pathEdges.isEmpty()) {
            throw new ValidationFailureException("Path 생성에 실패했습니다. (비어있는 간선)");
        }
    }

    public List<Station> toStations() {
        return stations;
    }

    public int toDistance() {
        return pathEdges.stream()
            .mapToInt(pathEdge -> (int) pathEdge.getWeight())
            .sum();
    }
}
