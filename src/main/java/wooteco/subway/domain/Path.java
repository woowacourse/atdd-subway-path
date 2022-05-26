package wooteco.subway.domain;

import wooteco.subway.exception.ClientException;
import java.util.*;

public class Path {

    private final List<SectionWeightEdge> edges;
    private final int distance;

    public Path(List<SectionWeightEdge> edges, int distance) {
        this.edges = edges;
        this.distance = distance;
    }

    public double calculateFare(List<Line> lines, Long age) {
        int lineExtraFare = findMostExpensiveExtraFare(lines);
        Fare fare = Fare.find(distance);
        return Age.find(age).calc(lineExtraFare + fare.calc(distance));
    }

    private int findMostExpensiveExtraFare(List<Line> lines) {
        return edges.stream()
                .mapToInt(edges -> findExtraFare(lines, edges.getLineId()))
                .max()
                .orElseThrow(NoSuchElementException::new);
    }

    private int findExtraFare(List<Line> lines, Long id) {
        return lines.stream()
                .filter(line -> line.getId() == id)
                .findAny()
                .orElseThrow(() -> new ClientException("존재하지 않은 line id입니다."))
                .getExtraFare();
    }

    public Set<Long> getStationIds() {
        Set<Long> stations = new LinkedHashSet<>();
        for (SectionWeightEdge edge : edges) {
            stations.add(edge.getUpStationId());
            stations.add(edge.getDownStationId());
        }
        return Collections.unmodifiableSet(stations);
    }

    public int getDistance() {
        return distance;
    }
}
