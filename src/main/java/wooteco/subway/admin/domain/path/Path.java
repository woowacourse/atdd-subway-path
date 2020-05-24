package wooteco.subway.admin.domain.path;

import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.LineStations;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.Stations;

// distance, duration을 연산하는 역할
public class Path {
    private final Stations stations;
    private final int distance;
    private final int duration;

    public Path(Stations stations, int distance, int duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public static Path of(LineStations lineStations, Stations pathStations) {
        Long preStationId = null;
        LineStations pathLineStations = new LineStations();

        for (Station station : pathStations.getStations()) {
            LineStation lineStation = lineStations.findLineStation(preStationId, station.getId());
            pathLineStations.add(lineStation);
            preStationId = station.getId();
        }

        return new Path(pathStations,
                pathLineStations.computeTotalDistance(),
                pathLineStations.computeTotalDuration());
    }

    public Stations getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
