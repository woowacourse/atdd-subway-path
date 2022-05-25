package wooteco.subway.controller.dto.request;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import wooteco.subway.service.dto.request.LineRequest;
import wooteco.subway.service.dto.request.PathRequest;
import wooteco.subway.service.dto.request.SectionRequest;
import wooteco.subway.service.dto.request.StationRequest;

@Component
public class RequestFormAssembler {

    public LineRequest lineRequest(LineRequestForm lineRequestForm) {
        return new LineRequest(List.of(sectionRequest(lineRequestForm)),
                lineRequestForm.getName(), lineRequestForm.getColor(), lineRequestForm.getExtraFare());
    }

    private SectionRequest sectionRequest(LineRequestForm lineRequestForm) {
        return new SectionRequest(lineRequestForm.getUpStationId(), lineRequestForm.getDownStationId(),
                lineRequestForm.getDistance());
    }

    public LineRequest lineRequest(LineUpdateRequestForm lineUpdateRequestForm) {
        return new LineRequest(Collections.emptyList(), lineUpdateRequestForm.getName(),
                lineUpdateRequestForm.getColor(), lineUpdateRequestForm.getExtraFare());
    }

    public SectionRequest sectionRequest(SectionRequestForm sectionRequestForm) {
        return new SectionRequest(
                sectionRequestForm.getUpStationId(), sectionRequestForm.getDownStationId(),
                sectionRequestForm.getDistance());
    }

    public StationRequest stationRequest(StationRequestForm stationRequestForm) {
        return new StationRequest(stationRequestForm.getName());
    }

    public PathRequest pathRequest(PathRequestForm pathRequestForm) {
        return new PathRequest(pathRequestForm.getSource(), pathRequestForm.getTarget());
    }
}
