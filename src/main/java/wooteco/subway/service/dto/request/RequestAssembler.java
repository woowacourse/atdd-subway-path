package wooteco.subway.service.dto.request;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.section.Section;
import wooteco.subway.domain.station.Station;

@Component
public class RequestAssembler {

    public Line temporaryLine(LineRequest lineRequest) {
        return new Line(sections(lineRequest.getSectionRequests()),
                lineRequest.getName(), lineRequest.getColor(), lineRequest.getExtraFare());
    }

    private List<Section> sections(List<SectionRequest> sectionRequests) {
        return sectionRequests.stream()
                .map(this::temporarySection)
                .collect(Collectors.toUnmodifiableList());
    }

    public Section temporarySection(SectionRequest sectionRequest) {
        return new Section(sectionRequest.getUpStationId(), sectionRequest.getDownStationId(),
                sectionRequest.getDistance());
    }

    public Station temporaryStation(StationRequest stationRequest) {
        return new Station(stationRequest.getName());
    }
}
