package wooteco.subway.domain.section;

import java.util.Objects;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.station.Station;

public class Section {

    private final Long id;
    private final Line line;
    private final Station upStation;
    private final Station downStation;
    private final int distance;

    public Section(final Long id, final Line line, final Station upStation, final Station downStation,
                   final int distance) {
        validatePositiveDistance(distance);
        validateDuplicateStation(upStation, downStation);
        this.id = id;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validatePositiveDistance(final int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("구간의 길이는 양수만 들어올 수 있습니다.");
        }
    }

    private void validateDuplicateStation(final Station upStation, final Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException("upstation과 downstation은 중복될 수 없습니다.");
        }
    }

    public Section(final Line line, final Station upStation, final Station downStation, final int distance) {
        this(null, line, upStation, downStation, distance);
    }

    public Section(final Long id, final Section section) {
        this(id, section.line, section.upStation, section.downStation, section.distance);
    }

    public boolean isUpperSection(final Section section) {
        return this.upStation.equals(section.downStation);
    }

    public boolean isLowerSection(final Section section) {
        return this.downStation.equals(section.upStation);
    }

    public boolean isConnectedSection(final Section section) {
        return containsStation(section.upStation) || containsStation(section.downStation);
    }

    public boolean containsStation(final Station station) {
        return isUpStation(station) || isDownStation(station);
    }

    public boolean isUpStation(final Station station) {
        return upStation.equals(station);
    }

    public boolean isDownStation(final Station station) {
        return downStation.equals(station);
    }

    public boolean equalsUpStation(final Section section) {
        return upStation.equals(section.upStation);
    }

    public boolean equalsDownStation(final Section section) {
        return downStation.equals(section.downStation);
    }

    public boolean isEqualsOrLargerDistance(final Section section) {
        return this.distance <= section.distance;
    }

    public Section createMiddleSectionByDownStationSection(final Section section) {
        return new Section(id, line, section.downStation, this.downStation, this.distance - section.distance);
    }

    public Section createMiddleSectionByUpStationSection(final Section section) {
        return new Section(id, line, this.upStation, section.upStation, this.distance - section.distance);
    }

    public Section createExtensionSection(final Section section) {
        return new Section(id, line, this.upStation, section.downStation, this.distance + section.distance);
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Section section = (Section) o;
        return Objects.equals(id, section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
