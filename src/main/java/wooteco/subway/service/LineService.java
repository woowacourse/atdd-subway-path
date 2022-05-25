package wooteco.subway.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.line.LineMap;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.request.CreateLineRequest;
import wooteco.subway.dto.request.UpdateLineRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class LineService {

    private static final String DUPLICATE_LINE_NAME_EXCEPTION_MESSAGE = "중복되는 이름의 지하철 노선이 존재합니다.";

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public List<LineResponse> findAll() {
        return lineRepository.findAllLines()
                .stream()
                .map(LineResponse::of)
                .sorted(Comparator.comparingLong(LineResponse::getId))
                .collect(Collectors.toUnmodifiableList());
    }

    public LineResponse find(Long id) {
        LineMap line = lineRepository.findExistingLine(id);
        return LineResponse.of(line);
    }

    @Transactional
    public LineResponse save(CreateLineRequest lineRequest) {
        String name = validateUniqueLineName(lineRequest.getName());
        String color = lineRequest.getColor();
        int extraFare = lineRequest.getExtraFare();
        Station upStation = stationRepository.findExistingStation(lineRequest.getUpStationId());
        Station downStation = stationRepository.findExistingStation(lineRequest.getDownStationId());
        Section newSection = new Section(upStation, downStation, lineRequest.getDistance());

        LineMap newLine = new LineMap(name, color, extraFare, newSection);
        return LineResponse.of(lineRepository.saveLine(newLine));
    }

    @Transactional
    public void update(Long id, UpdateLineRequest lineRequest) {
        LineMap line = lineRepository.findExistingLine(id);
        String name = validateUniqueLineName(lineRequest.getName());
        String color = lineRequest.getColor();
        int extraFare = lineRequest.getExtraFare();
        Sections sections = line.getSections();

        lineRepository.updateLine(new LineMap(id, name, color, extraFare, sections));
    }

    @Transactional
    public void delete(Long id) {
        LineMap line = lineRepository.findExistingLine(id);
        lineRepository.deleteLine(line);
    }

    private String validateUniqueLineName(String name) {
        boolean isDuplicateName = lineRepository.checkExistingLineName(name);
        if (isDuplicateName) {
            throw new IllegalArgumentException(DUPLICATE_LINE_NAME_EXCEPTION_MESSAGE);
        }
        return name;
    }
}
