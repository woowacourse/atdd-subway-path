package wooteco.subway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;


@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Section {
    private Long id;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;
    private Long lineId;

    public Section(Long upStationId, Long downStationId, Integer distance) {
        this(null, upStationId, downStationId, distance, null);
    }

    public Section(Long upStationId, Long downStationId, Integer distance, Long lineId) {
        this(null, upStationId, downStationId, distance, lineId);
    }

    public Section(Long id, Long upStationId, Long downStationId, Integer distance, Long lineId) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lineId = lineId;
    }
}
