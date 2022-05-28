package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.request.LineUpdateRequest;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.exception.DuplicateNameException;
import wooteco.subway.exception.NotFoundLineException;
import wooteco.subway.exception.NotFoundStationException;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository,
                       SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public LineResponse createLine(LineRequest lineRequest) {
        validateDuplicateName(lineRequest.getName());

        Line newLine = generateLineFromLineRequest(lineRequest);
        Long newLineId = lineRepository.save(newLine);
        Section section = generateSectionFromLineRequest(lineRequest);

        saveSections(List.of(section), newLineId);
        return LineResponse.from(newLineId, newLine, generateStationResponsesByLineId(newLineId));
    }

    private Line generateLineFromLineRequest(LineRequest lineRequest) {
        return new Line(lineRequest.getName(), lineRequest.getColor(), new Fare(lineRequest.getExtraFare()));
    }

    private Section generateSectionFromLineRequest(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId())
                .orElseThrow(NotFoundStationException::new);
        Station downStation = stationRepository.findById(lineRequest.getDownStationId())
                .orElseThrow(NotFoundStationException::new);
        return new Section(upStation, downStation, new Distance(lineRequest.getDistance()));
    }

    private void saveSections(List<Section> sections, Long lineId) {
        for (Section section : sections) {
            sectionRepository.save(lineId, section);
        }
    }

    private void validateDuplicateName(String name) {
        boolean isExisting = lineRepository.findByName(name).isPresent();

        if (isExisting) {
            throw new DuplicateNameException();
        }
    }

    public List<LineResponse> getAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(line -> LineResponse.from(line, generateStationResponsesByLineId(line.getId())))
                .collect(Collectors.toList());
    }

    public LineResponse getLineById(Long id) {
        return lineRepository.findById(id)
                .map(line -> LineResponse.from(line, generateStationResponsesByLineId(id)))
                .orElseThrow(NotFoundLineException::new);
    }

    public void update(Long id, LineUpdateRequest line) {
        validateExistingId(id);
        validateDuplicateName(line.getName());
        lineRepository.update(id, line.getName(), line.getColor());
    }

    public void delete(Long id) {
        validateExistingId(id);
        lineRepository.deleteById(id);
        sectionRepository.deleteAllSectionsByLineId(id);
    }

    private void validateExistingId(Long id) {
        boolean isExisting = lineRepository.findById(id).isPresent();

        if (!isExisting) {
            throw new NotFoundLineException();
        }
    }

    private List<StationResponse> generateStationResponsesByLineId(Long lineId) {
        Line foundLine = lineRepository.findById(lineId).orElseThrow(NotFoundLineException::new);
        List<Section> sections = foundLine.getSections().getValue();

        return Stream.concat(
                sections.stream().map(Section::getDownStation),
                sections.stream().map(Section::getUpStation)
        ).distinct().map(StationResponse::from).collect(Collectors.toList());
    }

    public void createSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).orElseThrow(NotFoundLineException::new);

        Station upStation = getStation(sectionRequest.getUpStationId());
        Station downStation = getStation(sectionRequest.getDownStationId());
        Distance distance = new Distance(sectionRequest.getDistance());

        line.addSection(new Section(upStation, downStation, distance));
        deleteAndCreateSections(lineId, line);
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(NotFoundStationException::new);
    }

    public void deleteStationById(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(NotFoundLineException::new);
        Station station = stationRepository.findById(stationId).orElseThrow(NotFoundStationException::new);

        line.deleteStation(station);
        deleteAndCreateSections(lineId, line);
    }

    private void deleteAndCreateSections(Long lineId, Line line) {
        sectionRepository.deleteAllSectionsByLineId(lineId);
        saveSections(line.getSections().getValue(), lineId);
    }
}
