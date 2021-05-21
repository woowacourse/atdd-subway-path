package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class Path {

    private final List<Station> stations;
    private final int distance;

    public Path(List<Station> stations, int distance) {
        validateStations(stations);
        validateDistance(distance);
        this.stations = stations;
        this.distance = distance;
    }

    private void validateStations(List<Station> stations) {
        if (stations.size() < 2) {
            throw new IllegalArgumentException("경로를 조회할 수 없습니다.");
        }
    }

    private void validateDistance(int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException("경로의 거리가 0보다 작을 수 없습니다.");
        }
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
