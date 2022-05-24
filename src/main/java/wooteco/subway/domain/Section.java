package wooteco.subway.domain;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Line line;
    private final Station upStation;
    private final Station downStation;
    private final int distance;

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this(null, line, upStation, downStation, distance);
    }

    public Section(Long id, Line line, Station upStation, Station downStation, int distance) {
        validate(line, upStation, downStation, distance);
        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validate(Line line, Station upStation, Station downStation, int distance) {
        validateLine(line);
        validateEndpoints(upStation, downStation);
        validateDistance(distance);
    }

    private void validateLine(Line line) {
        if (line == null) {
            throw new IllegalArgumentException("구간의 노선은 null일 수 없습니다.");
        }
    }

    private void validateEndpoints(Station upStation, Station downStation) {
        if (upStation == null || downStation == null) {
            throw new IllegalArgumentException("구간의 시작과 끝은 null일 수 없습니다.");
        }
        if (upStation.hasSameName(downStation)) {
            throw new IllegalArgumentException("구간의 시작과 끝은 같은 역일 수 없습니다.");
        }
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("구간 거리는 1보다 작을 수 없습니다.");
        }
    }

    public boolean hasSameStations(Section section) {
        return isSame(section) || isReversed(section);
    }

    private boolean isSame(Section section) {
        return upStation.hasSameName(section.upStation) && downStation.hasSameName(section.downStation);
    }

    private boolean isReversed(Section section) {
        return upStation.hasSameName(section.downStation) && downStation.hasSameName(section.upStation);
    }

    public boolean hasSameUpStation(Section section) {
        return this.upStation.hasSameName(section.getUpStation());
    }

    public boolean hasSameDownStation(Section section) {
        return this.downStation.hasSameName(section.getDownStation());
    }

    public boolean canInclude(Section section) {
        return this.distance - section.distance > 0;
    }

    public boolean containStation(Station station) {
        return upStation.hasSameName(station) || downStation.hasSameName(station);
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
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
        Section section = (Section) o;
        return distance == section.distance && Objects.equals(id, section.id) && Objects.equals(line,
                section.line) && Objects.equals(upStation, section.upStation) && Objects.equals(
                downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line, upStation, downStation, distance);
    }

}
