package wooteco.subway.controller.converter;

import wooteco.subway.controller.dto.request.SectionRequest;
import wooteco.subway.service.dto.request.SectionCreateRequest;
import wooteco.subway.service.dto.request.SectionDeleteRequest;

public class SectionConverter {
    public static SectionCreateRequest toInfo(Long lineId, SectionRequest sectionRequest) {
        return new SectionCreateRequest(lineId, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(),
            sectionRequest.getDistance());
    }

    public static SectionDeleteRequest toInfo(Long lineId, Long stationId) {
        return new SectionDeleteRequest(lineId, stationId);
    }
}
