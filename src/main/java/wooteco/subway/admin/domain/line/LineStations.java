package wooteco.subway.admin.domain.line;

import static java.util.stream.Collectors.*;

import java.util.Set;

public class LineStations {
    private final Set<LineStation> lineStations;

    public LineStations(Set<LineStation> lineStations) {
        this.lineStations = lineStations;
    }

    public Set<Long> getStationIds() {
        return lineStations.stream()
            .map(LineStation::getStationId)
            .collect(toSet());
    }

    public Set<LineStation> getStations() {
        return lineStations;
    }
}
