package wooteco.subway.domain;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final int distance;
    private final Line line;

    public Section(final Station upStation, final Station downStation, final int distance) {
        this(null, upStation, downStation, distance, null);
    }

    public Section(final Station upStation, final Station downStation, final int distance, final Line line) {
        this(null, upStation, downStation, distance, line);
    }

    public Section(final Long id, final Station upStation, final Station downStation, final int distance,
                   final Line line) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.line = line;
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Line getLine() {
        return line;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isGreaterOrEqualTo(final Section other) {
        return this.distance >= other.distance;
    }

    public Section merge(final Section section) {
        return new Section(this.upStation, section.downStation, sumDistance(section), this.line);
    }

    private int sumDistance(final Section section) {
        return this.distance + section.distance;
    }

    public Section createSectionInBetween(final Section section) {
        if (this.upStation.equals(section.upStation)) {
            return new Section(this.id, section.downStation, this.downStation, subtractDistance(section), this.line);
        }
        return new Section(this.id, this.upStation, section.upStation, subtractDistance(section), this.line);
    }

    private int subtractDistance(final Section section) {
        return this.distance - section.distance;
    }

    public boolean isSameValue(final Section section) {
        return this.getUpStation().equals(section.getUpStation()) && this.getDownStation().equals(section.getDownStation()) && this.distance == section.getDistance();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Section)) {
            return false;
        }
        final Section section = (Section) o;
        return distance == section.distance && Objects.equals(id, section.id) && Objects.equals(upStation,
                section.upStation) && Objects.equals(downStation, section.downStation) && Objects.equals(line, section.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStation, downStation, distance, line);
    }
}
