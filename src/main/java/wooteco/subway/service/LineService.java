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
        validateDuplicateNameOrColor(lineRequest.getName(), lineRequest.getColor());
        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getExtraFare());
        Station upStation = getStation(lineRequest.getUpStationId());
        Station downStation = getStation(lineRequest.getDownStationId());

        Line savedLine = lineRepository.save(line);
        sectionRepository
                .save(new Section(savedLine, upStation, downStation, lineRequest.getDistance()));

        return toLineResponse(savedLine, List.of(upStation, downStation));
    }

    public LineResponse showById(Long lineId) {
        return toLineResponse(getLine(lineId), getStations(lineId));
    }

    public List<LineResponse> showAll() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(line -> toLineResponse(line, getStations(line.getId())))
                .collect(Collectors.toList());
    }

    public void updateById(Long id, LineUpdateRequest request) {
        validateDuplicateNameOrColor(request.getName(), request.getColor());
        Line line = getLine(id);
        line.update(request.getName(), request.getColor(), request.getExtraFare());
        lineRepository.save(line);
    }

    public void removeById(Long id) {
        lineRepository.deleteById(id);
    }

    public void createSection(Long lineId, SectionRequest request) {
        Line line = getLine(lineId);
        Station upStation = getStation(request.getUpStationId());
        Station downStation = getStation(request.getDownStationId());
        int distance = request.getDistance();

        Sections sections = new Sections(sectionRepository.findSectionByLine(line));
        Section newSection = new Section(line, upStation, downStation, distance);
        for (Section updateSection : sections.findUpdateSections(newSection)) {
            sectionRepository.save(updateSection);
        }
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = getLine(lineId);
        Station station = getStation(stationId);

        Sections sections = new Sections(sectionRepository.findSectionByLine(line));
        List<Section> removedSections = sections.getDeleteSections(line, station);
        for (Section removedSection : removedSections) {
            isDeleteOrSave(removedSection);
        }
    }

    private void validateDuplicateNameOrColor(String name, String color) {
        if (lineRepository.existByNameOrColor(name, color)) {
            throw new BadRequestException("노선이 이름과 색상은 중복될 수 없습니다.");
        }
    }

    private void isDeleteOrSave(Section removedSection) {
        if (removedSection.getId() == null) {
            sectionRepository.save(removedSection);
        } else {
            sectionRepository.deleteById(removedSection.getId());
        }
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId);
    }

    private Line getLine(Long lineId) {
        return lineRepository.findById(lineId);
    }

    private LineResponse toLineResponse(Line line, List<Station> stations) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getExtraFare(),
                toResponse(stations)
        );
    }

    private static List<StationResponse> toResponse(List<Station> stations) {
        return stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    private List<Station> getStations(Long lineId) {
        Line line = lineRepository.findById(lineId);
        Sections sections = new Sections(sectionRepository.findSectionByLine(line));
        return sections.getStations();
    }
}

