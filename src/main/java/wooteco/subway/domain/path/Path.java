package wooteco.subway.domain.path;

import java.util.List;
import java.util.Objects;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.Stations;

public class Path {

    private final Stations stations;
    private final List<Line> lines;
    private final int distance;

    public Path(Stations stations, List<Line> lines, int distance) {
        validate(lines, distance);
        this.stations = stations;
        this.lines = lines;
        this.distance = distance;
    }

    private void validate(List<Line> lines, int distance) {
        validateLines(lines);
        validateDistance(distance);
    }

    private void validateLines(List<Line> lines) {
        if (lines == null || lines.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("지나간 노선들은 null일 수 없습니다.");
        }
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("경로 거리는 1보다 작을 수 없습니다.");
        }
    }

    public int getMostExpensiveExtraFare() {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }

    public List<Station> getStations() {
        return stations.getValues();
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Path path = (Path) o;
        return distance == path.distance && Objects.equals(stations, path.stations) && Objects.equals(
                lines, path.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, lines, distance);
    }

}
