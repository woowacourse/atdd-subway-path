package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Station;

public class Path {

    private final List<Station> stations;
    private final int distance;
    private final Lines usedLines;

    Path(List<Station> stations, int distance, Lines usedLines) {
        this.stations = stations;
        this.distance = distance;
        this.usedLines = usedLines;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int findMaxExtraFare() {
        return usedLines.findMaxExtraFare();
    }
}
