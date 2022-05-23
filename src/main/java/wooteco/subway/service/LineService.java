package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.exception.DomainException;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.service.dto.LineRequest;
import wooteco.subway.service.dto.LineResponse;
import wooteco.subway.service.dto.SectionSaveRequest;

@Service
@Transactional
public class LineService {

    private final StationService stationService;
    private final SectionService sectionService;
    private final LineRepository lineRepository;

    public LineService(StationService stationService, SectionService sectionService,
                       LineRepository lineRepository) {
        this.stationService = stationService;
        this.sectionService = sectionService;
        this.lineRepository = lineRepository;
    }

    public LineResponse save(final LineRequest request) {
        try {
            Line line = Line.withoutIdOf(request.getName(), request.getColor(),request.getExtraFare());
            Line saved = lineRepository.save(line);
            sectionService.save(new SectionSaveRequest(saved.getId(), request.getUpStationId(),
                    request.getDownStationId(), request.getDistance()));
            return createResponseFrom(saved);
        } catch (DuplicateKeyException e) {
            throw new DomainException(ExceptionMessage.DUPLICATED_LINE_NAME.getContent());
        }
    }

    private LineResponse createResponseFrom(Line line) {
        return LineResponse.of(line, sectionService.getSortedStationInLineId(line.getId()));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        return lineRepository.findAll().stream()
                .map(this::createResponseFrom)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findById(Long id) {
        Line line = lineRepository.findById(id);
        List<Station> sortedStations = sectionService.getSortedStationInLineId(line.getId());
        return LineResponse.of(line, sortedStations);
    }

    public void updateById(final Long id, final LineRequest request) {
        Line updated = new Line(id, request.getName(), request.getColor());
        lineRepository.update(updated);
    }

    public void deleteById(final Long id) {
        lineRepository.deleteById(id);
    }
}
