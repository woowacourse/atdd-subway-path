package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.BadRequestException;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.service.dto.request.LineRequest;
import wooteco.subway.service.dto.request.LineUpdateRequest;
import wooteco.subway.service.dto.request.SectionRequest;
import wooteco.subway.service.dto.response.LineResponse;
import wooteco.subway.service.dto.response.StationResponse;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(
            LineRepository lineRepository,
            StationRepository stationRepository,
            SectionRepository sectionRepository
    ) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public LineResponse create(LineRequest lineRequest) {
        validateDuplicateNameAndColor(lineRequest.getName(), lineRequest.getColor());
        Line line = new Line(lineRequest.getName(), lineRequest.getColor());
        Station upStation = findStation(lineRequest.getUpStationId());
        Station downStation = findStation(lineRequest.getDownStationId());

        Line savedLine = lineRepository.save(line);
        sectionRepository.save(new Section(savedLine, upStation, downStation, lineRequest.getDistance()));

        return toLineResponse(savedLine, List.of(upStation, downStation));
    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId);
    }

    private void validateDuplicateNameAndColor(String name, String color) {
        if (lineRepository.existByNameAndColor(name, color)) {
            throw new BadRequestException("노선이 이름과 색상은 중복될 수 없습니다.");
        }
    }

    private LineResponse toLineResponse(Line line, List<Station> stations) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), toResponse(stations));
    }

    private static List<StationResponse> toResponse(List<Station> stations) {
        return stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    public LineResponse showById(Long lineId) {
        return toLineResponse(findLine(lineId), getStations(lineId));
    }

    private Line findLine(Long lineId) {
        return lineRepository.findById(lineId);
    }

    private List<Station> getStations(Long lineId) {
        Line line = lineRepository.findById(lineId);
        Sections sections = new Sections(sectionRepository.findSectionByLine(line));
        return sections.getStations();
    }

    public List<LineResponse> showAll() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(line -> toLineResponse(line, getStations(line.getId())))
                .collect(Collectors.toList());
    }

    public void updateById(Long id, LineUpdateRequest request) {
        validateDuplicateNameAndColor(request.getName(), request.getColor());
        Line line = findLine(id);
        line.update(request.getName(), request.getColor());
        lineRepository.save(line);
    }

    public void removeById(Long id) {
        lineRepository.deleteById(id);
    }

    public void createSection(Long lineId, SectionRequest request) {
        Line line = findLine(lineId);
        Station upStation = findStation(request.getUpStationId());
        Station downStation = findStation(request.getDownStationId());
        int distance = request.getDistance();
        Sections sections = new Sections(sectionRepository.findSectionByLine(line));
        Section newSection = new Section(line, upStation, downStation, distance);

        for (Section updateSection : sections.findUpdatedSections(newSection)) {
            sectionRepository.save(updateSection);
        }
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = findLine(lineId);
        Station station = findStation(stationId);
        Sections sections = new Sections(sectionRepository.findSectionByLine(line));

        List<Section> removedSections = sections.findDeleteSections(line, station);
        for (Section removedSection : removedSections) {
            sectionRepository.deleteById(removedSection.getId());
        }

        if (removedSections.size() == Sections.COMBINE_SIZE) {
            Section combineSection = sections.combine(line, removedSections);
            sectionRepository.save(combineSection);
        }
    }
}
