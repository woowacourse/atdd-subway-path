package wooteco.subway.domain.section;

import wooteco.subway.domain.station.Station;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Section {
    private final Long id;
    private final Long lineId;
    private final Station upStation;
    private final Station downStation;
    private final int distance;

    public Section(Long id, Long lineId, Station upStation, Station downStation, int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Long lineId, Station upStation, Station downStation, int distance) {
        this(null, lineId, upStation, downStation, distance);
    }

    public Section revisedBy(Section section) {
        int revisedDistance = distance - section.getDistance();

        if (Objects.equals(upStation, section.getUpStation())) {
            return new Section(id, section.getLineId(), section.getDownStation(), downStation, revisedDistance);
        }

        return new Section(id, section.getLineId(), upStation, section.getUpStation(), revisedDistance);
    }

    public boolean isLongerThan(Section section) {
        return distance >= section.getDistance();
    }

    public boolean isConnectedTo(Section section) {
        return isOnSameLine(section)
                && hasCommonStationWith(section);
    }

    public boolean isOverLappedWith(Section section) {
        return isOnSameLine(section)
                && (upStation.equals(section.getUpStation()) || downStation.equals(section.getDownStation()));
    }

    public boolean hasStation(Station station) {
        return List.of(upStation, downStation).contains(station);
    }

    public boolean isOnSameLine(Section section) {
        return lineId.equals(section.lineId);
    }

    private boolean hasCommonStationWith(Section section) {
        return !Collections.disjoint(List.of(upStation, downStation),
                List.of(section.getUpStation(), section.getDownStation()));
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return distance == section.distance
                && Objects.equals(lineId, section.lineId)
                && Objects.equals(upStation, section.upStation)
                && Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineId, upStation, downStation, distance);
    }
}
