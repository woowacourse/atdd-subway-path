package wooteco.subway.domain.path;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class Path {

    private static final String SELF_LOOP_EXCEPTION = "출발점과 도착점은 동일할 수 없습니다.";

    private final Station source;
    private final List<Section> route;

    private Path(Station source, List<Section> route) {
        this.source = source;
        this.route = route;
    }

    public static Path of(Station source, Station target, Navigator<Station, Section> navigator) {
        validateNonSelfLoop(source, target);
        return new Path(source, navigator.calculateShortestPath(source, target));
    }

    private static void validateNonSelfLoop(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException(SELF_LOOP_EXCEPTION);
        }
    }

    public List<Station> toStations() {
        List<Station> stations = new ArrayList<>();
        stations.add(source);
        for (Section section : route) {
            Station lastStation = stations.get(stations.size() - 1);
            stations.add(section.getOppositeEnd(lastStation));
        }
        return stations;
    }

    public int getDistance() {
        return route.stream()
                .mapToInt(Section::getDistance)
                .sum();
    }

    public List<Long> getPassingLineIds() {
        return route.stream()
                .map(Section::getLineId)
                .distinct()
                .collect(Collectors.toList());
    }
}
