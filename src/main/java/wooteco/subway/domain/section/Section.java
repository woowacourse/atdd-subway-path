package wooteco.subway.domain.section;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Section {
    private Long id;
    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public Section(Long id, Long lineId, Long upStationId, Long downStationId, int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section(Long lineId, Long upStationId, Long downStationId, int distance) {
        this(null, lineId, upStationId, downStationId, distance);
    }

    public Section revisedBy(Section section) {
        int revisedDistance = distance - section.getDistance();

        if (Objects.equals(upStationId, section.getUpStationId())) {
            return new Section(id, lineId, section.getDownStationId(), downStationId, revisedDistance);
        }

        return new Section(id, lineId, upStationId, section.getUpStationId(), revisedDistance);
    }

    public boolean isLongerThan(Section section) {
        return distance >= section.getDistance();
    }

    private boolean isOnSameLine(Section section) {
        return lineId.equals(section.getLineId());
    }

    private boolean hasCommonStationWith(Section section) {
        return !Collections.disjoint(List.of(upStationId, downStationId),
                List.of(section.getUpStationId(), section.getDownStationId()));
    }

    public boolean isConnectedTo(Section section) {
        return isOnSameLine(section) && hasCommonStationWith(section);
    }

    public boolean isOverLappedWith(Section section) {
        return isOnSameLine(section)
                && (upStationId.equals(section.getUpStationId()) || downStationId.equals(section.getDownStationId()));
    }

    public boolean hasStation(Long stationId) {
        return List.of(upStationId, downStationId).contains(stationId);
    }

    public boolean hasSameValue(Section section) {
        return distance == section.distance
                && Objects.equals(upStationId, section.upStationId)
                && Objects.equals(downStationId, section.downStationId)
                && Objects.equals(id, section.id);
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
