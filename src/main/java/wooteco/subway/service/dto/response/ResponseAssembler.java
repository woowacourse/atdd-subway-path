package wooteco.subway.service.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.section.Section;
import wooteco.subway.domain.line.Fare;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.station.Station;

@Component
public class ResponseAssembler {

    public List<LineResponse> lineResponses(List<Line> lines) {
        return lines.stream()
                .map(this::lineResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineResponse lineResponse(Line line) {
        return new LineResponse(line.getId(), sectionResponses(line.getSections()),
                line.getName(), line.getColor(), line.getExtraFare());
    }

    private List<SectionResponse> sectionResponses(List<Section> sections) {
        return sections.stream()
                .map(this::sectionResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    private SectionResponse sectionResponse(Section section) {
        return new SectionResponse(section.getId(), section.getUpStationId(), section.getDownStationId(),
                section.getDistance());
    }

    public List<StationResponse> stationResponses(List<Station> stations) {
        return stations.stream()
                .map(this::stationResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public StationResponse stationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public List<Line> lines(List<LineResponse> lineResponses) {
        return lineResponses.stream()
                .map(this::line)
                .collect(Collectors.toUnmodifiableList());
    }

    private Line line(LineResponse lineResponse) {
        return new Line(lineResponse.getId(), sections(lineResponse.getSectionResponses()),
                lineResponse.getName(), lineResponse.getColor(), lineResponse.getExtraFare());
    }

    private List<Section> sections(List<SectionResponse> sectionResponses) {
        return sectionResponses.stream()
                .map(this::section)
                .collect(Collectors.toUnmodifiableList());
    }

    private Section section(SectionResponse sectionResponse) {
        return new Section(sectionResponse.getId(), sectionResponse.getUpStationId(),
                sectionResponse.getDownStationId(), sectionResponse.getDistance());
    }

    public PathResponse pathResponse(Path path, Fare fare) {
        return new PathResponse(path.getPath(), path.getDistance(), fare.getFare());
    }
}
