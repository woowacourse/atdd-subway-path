package wooteco.subway.dao.dto;

import wooteco.subway.domain.Section;

public class SectionInsertDto {
    private final long lineId;
    private final long upStationId;
    private final long downStationId;
    private final int distance;
    private final int indexNum;

    public SectionInsertDto(Section section, long lineId, int indexNum) {
        this(lineId, section.getUpStationId(), section.getDownStationId(), section.getDistance(), indexNum);
    }

    public SectionInsertDto(long lineId, long upStationId, long downStationId, int distance, int indexNum) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.indexNum = indexNum;
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
