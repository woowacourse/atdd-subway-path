package wooteco.subway.domain.path;

import java.util.Collections;
import java.util.List;
import wooteco.subway.domain.fare.Age;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.domain.station.Station;

public class Path {

    private final List<Station> stations;
    private final List<Line> lines;
    private final Distance distance;

    public Path(List<Station> stations, List<Line> lines, Distance distance) {
        this.stations = stations;
        this.lines = lines;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public Fare getFare(Age age) {
        return Fare.of(distance, age, generateExtraFare());
    }

    private Fare generateExtraFare() {
        System.out.println(lines);

        int maxFare = lines.stream()
                .map(Line::getExtraFare)
                .mapToInt(Fare::getValue)
                .max()
                .orElse(0);

        return new Fare(maxFare);
    }

    public Distance getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Path{" +
                "stations=" + stations +
                ", lines=" + lines +
                ", distance=" + distance +
                '}';
    }
}
