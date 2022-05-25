package wooteco.subway.domain.path;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.fare2.Fare;
import wooteco.subway.domain.line.LineExtraFare;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

public class Path {

    private static final String SELF_LOOP_EXCEPTION = "출발점과 도착점은 동일할 수 없습니다.";

    private final Station source;
    private final List<Section> route;

    public Path(Station source, Station target, Navigator navigator) {
        validateNonSelfLoop(source, target);
        this.source = source;
        this.route = navigator.calculateShortestPath(source, target);
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

    public int calculateFare(List<LineExtraFare> extraFares, int age) {
        Fare fare = new Fare();
        fare = fare.applyDistanceOverFarePolicies(getDistance());
        fare = fare.applyMaximumLineExtraFare(extraFares);
        fare = fare.applyAgeDiscountPolicy(age);

        return fare.toInt();
    }
}
