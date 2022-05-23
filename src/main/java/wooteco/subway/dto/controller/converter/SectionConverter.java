package wooteco.subway.dto.controller.converter;

import wooteco.subway.dto.service.request.SectionCreateRequest;
import wooteco.subway.dto.service.request.SectionDeleteRequest;
import wooteco.subway.dto.controller.request.SectionRequest;

public class SectionConverter {

    public static SectionCreateRequest toInfo(Long lineId, SectionRequest sectionRequest) {
        return new SectionCreateRequest(lineId, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(),
            sectionRequest.getDistance());
    }

    public static SectionDeleteRequest toInfo(Long lineId, Long stationId) {
        return new SectionDeleteRequest(lineId, stationId);
    }
}
