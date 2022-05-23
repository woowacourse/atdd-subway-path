package wooteco.subway.dto.request;

import lombok.*;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;
    private Integer extraFare;

    public Line toLine() {
        return new Line(
                this.getName(),
                this.getColor(),
                this.getExtraFare()
        );
    }

    public Section toSection() {
        return new Section(
                this.getUpStationId(),
                this.getDownStationId(),
                this.getDistance()
        );
    }
}
