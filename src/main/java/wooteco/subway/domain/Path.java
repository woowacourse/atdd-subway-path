package wooteco.subway.domain;

import java.util.List;
import wooteco.subway.domain.path.PathCalculator;
import wooteco.subway.domain.path.ShortestPathEdge;
import wooteco.subway.exception.SubwayException;

public class Path {

    private static final int DEFAULT_DISTANCE = 10;
    private static final int DEFAULT_FARE = 1250;
    private static final int OVER_FARE_DISTANCE = 50;
    private static final int STANDARD_UNIT = 5;
    private static final int MAX_UNIT = 8;

    private final PathCalculator pathCalculator;
    private final Station startStation;
    private final Station endStation;

    public Path(final PathCalculator pathCalculator,
                final Station startStation,
                final Station endStation) {
        validateDifferentStation(startStation, endStation);
        this.pathCalculator = pathCalculator;
        this.startStation = startStation;
        this.endStation = endStation;
    }

    private void validateDifferentStation(final Station startStation, final Station endStation) {
        if (startStation.equals(endStation)) {
            throw new SubwayException("[ERROR] 최소 경로 탐색이 불가합니다.");
        }
    }

    public int calculateMinDistance(final Sections sections) {
        return pathCalculator.calculateShortestDistance(sections, startStation, endStation);
    }

    public List<Station> findShortestStations(final Sections sections) {
        return pathCalculator.calculateShortestStations(sections, startStation, endStation);
    }

    public List<ShortestPathEdge> findPassedEdges(final Sections sections) {
        return pathCalculator.findPassedEdges(sections, startStation, endStation);
    }

    public int calculateFare(final Sections sections) {
        int distance = calculateMinDistance(sections);
        if (distance <= DEFAULT_DISTANCE) {
            return DEFAULT_FARE;
        }
        if (distance <= OVER_FARE_DISTANCE) {
            return DEFAULT_FARE + calculateOverFare(distance - DEFAULT_DISTANCE, STANDARD_UNIT);
        }
        return DEFAULT_FARE
                + calculateOverFare(OVER_FARE_DISTANCE - DEFAULT_DISTANCE, STANDARD_UNIT)
                + calculateOverFare(distance - OVER_FARE_DISTANCE, MAX_UNIT);
    }

    private int calculateOverFare(final int distance, final int unit) {
        return (int) ((Math.ceil((distance - 1) / unit) + 1) * 100);
    }


}
