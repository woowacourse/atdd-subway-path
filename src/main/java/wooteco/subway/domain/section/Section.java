package wooteco.subway.domain.section;

import java.util.Objects;
import wooteco.subway.domain.station.Station;

public class Section {

    private static final int MIN_DISTANCE = 1;
    private static final String INVALID_DISTANCE_ERROR_MESSAGE = String.format("거리는 %d 이상이어야 합니다.", MIN_DISTANCE);
    private static final String DUPLICATED_SECTIONS_ERROR_MESSAGE = "상행과 하행은 같은 역으로 등록할 수 없습니다.";

    private final Long id;
    private final Long lineId;
    private Station upStation;
    private final Station downStation;
    private int distance;

    public Section(Long id, Long lineId, Station upStation, Station downStation, int distance) {
        validSection(upStation, downStation, distance);
        this.id = id;
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Long lineId, Station upStation, Station downStation, int distance) {
        this(null, lineId, upStation, downStation, distance);
    }

    private void validSection(Station upStation, Station downStation, int distance) {
        validDistance(distance);
        validStations(upStation, downStation);
    }

    private void validDistance(int distance) {
        if (distance < MIN_DISTANCE) {
            throw new IllegalArgumentException(INVALID_DISTANCE_ERROR_MESSAGE);
        }
    }

    private void validStations(Station station, Station otherStation) {
        if (station.isSameId(otherStation)) {
            throw new IllegalArgumentException(DUPLICATED_SECTIONS_ERROR_MESSAGE);
        }
    }

    public void reduceDistance(Section other) {
        int changedDistance = distance - other.distance;
        validDistance(changedDistance);
        this.distance = changedDistance;
    }

    public void addDistance(Section other) {
        this.distance += other.distance;
    }

    public boolean isSameDownStationId(Section section) {
        return isSameDownStationId(section.downStation.getId());
    }

    public boolean isSameDownStationId(Long id) {
        return id.equals(downStation.getId());
    }

    public boolean isSameUpStationId(Section section) {
        return isSameUpStationId(section.upStation.getId());
    }

    public boolean isSameUpStationId(Long id) {
        return id.equals(upStation.getId());
    }

    public void updateUpStationId(Station newStation) {
        validStations(newStation, downStation);
        this.upStation = newStation;
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof Section)) {
            return false;
        }
        Section section = (Section) o;
        return distance == section.distance && Objects.equals(id, section.id) && Objects.equals(lineId,
                section.lineId) && Objects.equals(upStation, section.upStation) && Objects.equals(
                downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lineId, upStation, downStation, distance);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", lineId=" + lineId +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }
}
