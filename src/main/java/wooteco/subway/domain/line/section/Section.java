package wooteco.subway.domain.line.section;

import java.util.List;

import wooteco.subway.domain.Id;

public class Section {

    private final Id id;
    private final Id upStationId;
    private final Id downStationId;
    private final Distance distance;

    public Section(Id id, Id upStationId, Id downStationId, Distance distance) {
        validateStationsNotSame(upStationId, downStationId);
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section(long id, long upStationId, long downStationId, long distance) {
        this(new Id(id), new Id(upStationId), new Id(downStationId), new Distance(distance));
    }

    private Section(Id upStationId, Id downStationId, Distance distance) {
        this(Id.temporary(), upStationId, downStationId, distance);
    }

    public Section(long upStationId, long downStationId, long distance) {
        this(new Id(upStationId), new Id(downStationId), new Distance(distance));
    }

    private void validateStationsNotSame(Id upStationId, Id downStationId) {
        if (upStationId.equals(downStationId)) {
            throw new IllegalArgumentException("상행역과 하행역은 동일할 수 없습니다.");
        }
    }

    public boolean containsUpStationOf(Section section) {
        return equalsUpStation(section) || isPreviousOf(section);
    }

    public boolean containsDownStationOf(Section section) {
        return equalsDownStation(section) || isNextOf(section);
    }

    public boolean isPreviousOf(Section section) {
        return downStationId.equals(section.upStationId);
    }

    public boolean isNextOf(Section section) {
        return upStationId.equals(section.downStationId);
    }

    public boolean equalsUpStation(Section section) {
        return containsAsUpStation(section.upStationId);
    }

    public boolean containsAsUpStation(long stationId) {
        return containsAsUpStation(new Id(stationId));
    }

    public boolean containsAsUpStation(Id stationId) {
        return stationId.equals(upStationId);
    }

    public boolean equalsDownStation(Section section) {
        return containsAsDownStation(section.downStationId);
    }

    public boolean containsAsDownStation(long stationId) {
        return containsAsDownStation(new Id(stationId));
    }

    public boolean containsAsDownStation(Id stationId) {
        return stationId.equals(downStationId);
    }

    public List<Section> split(Section section) {
        if (equalsUpStation(section)) {
            Section fragment = new Section(section.downStationId, downStationId, subtractDistance(section));
            return List.of(section, fragment);
        }
        if (equalsDownStation(section)) {
            Section fragment = new Section(upStationId, section.upStationId, subtractDistance(section));
            return List.of(fragment, section);
        }
        throw new IllegalArgumentException("상행역과 하행역이 일치하지 않습니다.");
    }

    private Distance subtractDistance(Section section) {
        validateDistanceIsEnoughToSplit(section);
        return distance.subtract(section.distance);
    }

    private void validateDistanceIsEnoughToSplit(Section section) {
        if (!distance.isLongerThan(section.distance)) {
            throw new IllegalArgumentException("기존 구간의 거리보다 길거나 같습니다.");
        }
    }

    public Section merge(Section section) {
        if (isPreviousOf(section)) {
            return new Section(upStationId, section.downStationId, sumDistance(section));
        }
        if (isNextOf(section)) {
            return new Section(section.upStationId, downStationId, sumDistance(section));
        }
        throw new IllegalArgumentException("두 구간은 이어지지 않았습니다.");
    }

    private Distance sumDistance(Section section) {
        return distance.sum(section.distance);
    }

    public boolean isTemporary() {
        return id.isTemporary();
    }

    public long getId() {
        return id.getId();
    }

    public long getUpStationId() {
        return upStationId.getId();
    }

    public long getDownStationId() {
        return downStationId.getId();
    }

    public long getDistance() {
        return distance.getDistance();
    }
}
