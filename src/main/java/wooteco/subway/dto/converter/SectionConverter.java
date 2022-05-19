package wooteco.subway.dto.converter;

import wooteco.subway.dto.controller.request.SectionRequest;
import wooteco.subway.dto.service.SectionCreateRequest;
import wooteco.subway.dto.service.SectionDeleteRequest;

public class SectionConverter {

    public static SectionCreateRequest toServiceRequest(Long lineId, SectionRequest sectionRequest) {
        return new SectionCreateRequest(lineId, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(),
                sectionRequest.getDistance());
    }

    public static SectionDeleteRequest toServiceRequest(Long lineId, Long stationId) {
        return new SectionDeleteRequest(lineId, stationId);
    }
}
