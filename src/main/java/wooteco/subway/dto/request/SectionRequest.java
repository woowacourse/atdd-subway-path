package wooteco.subway.dto.request;

import lombok.*;
import wooteco.subway.domain.Section;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SectionRequest {

    public Long upStationId;
    public Long downStationId;
    public Integer distance;

    public Section toSection(Long lineId) {
        return new Section(
                this.getUpStationId(),
                this.getDownStationId(),
                this.getDistance(),
                lineId
        );
    }
}
