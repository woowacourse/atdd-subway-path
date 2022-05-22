package wooteco.subway.domain.section;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import wooteco.subway.domain.station.Station;

public class Section {
    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public Section(Long id, Station upStation, Station downStation, Distance distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Station upStation, Station downStation, Distance distance) {
        this(null, upStation, downStation, distance);
    }

    public Section(Long id, Section section) {
        this(id, section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    public static Section merge(Section upSection, Section downSection) {
        return new Section(upSection.upStation, downSection.downStation, upSection.addDistance(downSection));
    }

    public boolean isUpStationSame(Section other) {
        return upStation.equals(other.upStation);
    }

    public boolean isDownStationSame(Section other) {
        return downStation.equals(other.downStation);
    }

    public boolean isEitherUpStationOrDownStationSame(Section other) {
        return isUpStationSame(other) || isDownStationSame(other);
    }

    public boolean isDistanceLessThanOrEqualTo(Section other) {
        return distance.isLessThanOrEqualTo(other.distance);
    }

    public boolean hasSameStation(Section other) {
        HashSet<Station> stations = new HashSet<>(List.of(upStation, downStation, other.upStation, other.downStation));
        return stations.size() < 4;
    }

    public boolean hasStationIdAsUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean hasStationIdAsDownStation(Station station) {
        return downStation.equals(station);
    }

    public Distance addDistance(Section other) {
        return distance.add(other.distance);
    }

    public Distance subtractDistance(Section other) {
        return distance.subtract(other.distance);
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

    public Distance getDistance() {
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
        return Objects.equals(id, section.id) && Objects.equals(upStation, section.upStation)
                && Objects.equals(downStation, section.downStation) && Objects.equals(distance,
                section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStation, downStation, distance);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }
}
