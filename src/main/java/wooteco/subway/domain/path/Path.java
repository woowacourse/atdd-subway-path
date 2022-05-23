package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.element.Line;
import wooteco.subway.domain.element.Station;

public class Path {
    private final List<Station> stations;
    private final int distance;
    private final List<Line> lines;

    private Path(List<Station> stations, int distance, List<Line> lines) {
        this.stations = stations;
        this.distance = distance;
        this.lines = lines;
    }

    public static Path create(SubwayGraph graph, Station source, Station target) {
        return new Path(graph.getShortestRoute(source, target),
                graph.getShortestDistance(source, target),
                graph.getLines(source, target)
        );
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public List<Line> getLines() {
        return lines;
    }
}
