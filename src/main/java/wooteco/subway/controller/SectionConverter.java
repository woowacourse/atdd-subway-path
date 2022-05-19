package wooteco.subway.controller;

import wooteco.subway.dto.controller.request.SectionRequest;
import wooteco.subway.dto.service.SectionCreateRequest;
import wooteco.subway.dto.service.SectionDeleteRequest;

public class SectionConverter {

    static SectionCreateRequest toServiceRequest(Long lineId, SectionRequest sectionRequest) {
        return new SectionCreateRequest(lineId, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(),
            sectionRequest.getDistance());
    }

    static SectionDeleteRequest toServiceRequest(Long lineId, Long stationId) {
        return new SectionDeleteRequest(lineId, stationId);
    }
}
