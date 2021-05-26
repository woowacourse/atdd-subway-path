package wooteco.subway.path.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import wooteco.subway.exception.ValidationFailureException;
import wooteco.subway.station.domain.Station;

public class Path {

    private final List<PathEdge> pathEdges;

    public Path(List<PathEdge> pathEdges) {
        validateExistence(pathEdges);
        this.pathEdges = new ArrayList<>(pathEdges);
    }

    private void validateExistence(List<PathEdge> pathEdges) {
        if (Objects.isNull(pathEdges) || pathEdges.isEmpty()) {
            throw new ValidationFailureException("구간이 없는 경로는 생성할 수 없습니다.");
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
