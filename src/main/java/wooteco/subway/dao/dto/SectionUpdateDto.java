package wooteco.subway.dao.dto;

import wooteco.subway.domain.Section;

public class SectionUpdateDto {
    private final long id;

    private final long lineId;
    private final long upStationId;
    private final long downStationId;
    private final int distance;
    private final int indexNum;

    public SectionUpdateDto(Section section, long lineId, int indexNum) {
        this(section.getId(), lineId, section.getUpStationId(), section.getDownStationId(), section.getDistance(),
                indexNum);
    }

    public SectionUpdateDto(long id, long lineId, long upStationId, long downStationId, int distance, int indexNum) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.indexNum = indexNum;
    }

    public long getId() {
        return id;
    }

    public long getLineId() {
        return lineId;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getIndexNum() {
        return indexNum;
    }
}
