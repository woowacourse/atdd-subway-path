package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.line.LineMap;
import wooteco.subway.domain.line.LineInfo;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.request.CreateLineRequest;
import wooteco.subway.dto.request.UpdateLineRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.exception.ExceptionType;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class LineService {

    private static final String DUPLICATE_LINE_NAME_EXCEPTION_MESSAGE = "중복되는 이름의 지하철 노선이 존재합니다.";

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public List<LineResponse> findAll() {
        return Lines.of(lineRepository.findAllLines(), sectionRepository.findAllSections())
                .toSortedList()
                .stream()
                .map(LineResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineResponse find(Long id) {
        LineInfo lineInfo = lineRepository.findExistingLine(id);
        Sections sections = new Sections(sectionRepository.findAllSectionsByLineId(id));
        return LineResponse.of(new LineMap(lineInfo, sections));
    }

    @Transactional
    public LineResponse save(CreateLineRequest lineRequest) {
        validateUniqueLineName(lineRequest.getName());
        Station upStation = stationRepository.findExistingStation(lineRequest.getUpStationId());
        Station downStation = stationRepository.findExistingStation(lineRequest.getDownStationId());
        Section newSection = new Section(upStation, downStation, lineRequest.getDistance());

        LineInfo newLine = new LineInfo(lineRequest.getName(), lineRequest.getColor(), lineRequest.getExtraFare());
        return LineResponse.of(lineRepository.saveLine(newLine, newSection));
    }

    @Transactional
    public void update(Long id, UpdateLineRequest lineRequest) {
        String name = lineRequest.getName();
        validateExistingLine(id);
        validateUniqueLineName(name);
        lineRepository.updateLine(new LineInfo(id, name, lineRequest.getColor(), lineRequest.getExtraFare()));
    }

    @Transactional
    public void delete(Long id) {
        LineInfo line = lineRepository.findExistingLine(id);
        lineRepository.deleteLine(line);
    }

    private void validateExistingLine(Long id) {
        boolean isExistingLine = lineRepository.checkExistingLine(id);
        if (!isExistingLine) {
            throw new NotFoundException(ExceptionType.LINE_NOT_FOUND);
        }
    }

    private void validateUniqueLineName(String name) {
        boolean isDuplicateName = lineRepository.checkExistingLineName(name);
        if (isDuplicateName) {
            throw new IllegalArgumentException(DUPLICATE_LINE_NAME_EXCEPTION_MESSAGE);
        }
    }
}
