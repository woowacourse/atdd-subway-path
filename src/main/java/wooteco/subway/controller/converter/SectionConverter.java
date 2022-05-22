package wooteco.subway.controller.converter;

import wooteco.subway.dto.info.SectionCreateRequest;
import wooteco.subway.dto.info.SectionDeleteRequest;
import wooteco.subway.dto.request.SectionRequest;

public class SectionConverter {

    public static SectionCreateRequest toInfo(Long lineId, SectionRequest sectionRequest) {
        return new SectionCreateRequest(lineId, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(),
            sectionRequest.getDistance());
    }

    public static SectionDeleteRequest toInfo(Long lineId, Long stationId) {
        return new SectionDeleteRequest(lineId, stationId);
    }
}
