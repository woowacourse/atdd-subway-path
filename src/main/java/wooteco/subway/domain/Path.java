package wooteco.subway.domain;

import wooteco.subway.exception.SubwayException;

public class Path {

    private final Station startStation;
    private final Station endStation;

    public Path(final Station startStation,
                final Station endStation) {
        validateDifferentStation(startStation, endStation);
        this.startStation = startStation;
        this.endStation = endStation;
    }

    private void validateDifferentStation(final Station startStation, final Station endStation) {
        if (startStation.equals(endStation)) {
            throw new SubwayException("[ERROR] 최소 경로 탐색이 불가합니다.");
        }
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }
}
