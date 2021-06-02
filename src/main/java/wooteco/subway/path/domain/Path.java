package wooteco.subway.path.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import wooteco.subway.exception.application.ValidationFailureException;
import wooteco.subway.path.exception.RoutingFailureException;
import wooteco.subway.station.domain.Station;

public class Path {

    private final List<PathEdge> pathEdges;

    public Path(List<PathEdge> pathEdges) {
        validateNullOrEmpty(pathEdges);

        this.pathEdges = new ArrayList<>(pathEdges);
    }

    private void validateNullOrEmpty(List<PathEdge> pathEdges) {
        if (Objects.isNull(pathEdges) || pathEdges.isEmpty()) {
            throw new ValidationFailureException("Path 생성에 실패했습니다. (비어있는 간선)");
        }
    }

    public List<Station> toStations() {
        return pathEdges.stream()
            .flatMap(pathEdge -> Stream.of(pathEdge.getSource(), pathEdge.getTarget()))
            .distinct()
            .collect(Collectors.toList());
    }

    public int toDistance() {
        return pathEdges.stream()
            .mapToInt(pathEdge -> (int) pathEdge.getWeight())
            .sum();
    }
}
