package wooteco.subway.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.exception.DuplicateLineColorException;
import wooteco.subway.exception.DuplicateLineNameException;
import wooteco.subway.service.dto.request.LineRequest;
import wooteco.subway.service.dto.request.RequestAssembler;
import wooteco.subway.service.dto.request.SectionRequest;
import wooteco.subway.service.dto.response.LineResponse;
import wooteco.subway.service.dto.response.ResponseAssembler;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final IntegrityValidator integrityValidator;
    private final RequestAssembler requestAssembler;
    private final ResponseAssembler responseAssembler;

    public LineService(LineRepository lineRepository, IntegrityValidator integrityValidator,
                       RequestAssembler requestAssembler, ResponseAssembler responseAssembler) {
        this.lineRepository = lineRepository;
        this.integrityValidator = integrityValidator;
        this.requestAssembler = requestAssembler;
        this.responseAssembler = responseAssembler;
    }

    public LineResponse create(LineRequest lineRequest) {
        validateStationsExist(lineRequest);
        validateLineNameNotDuplicated(lineRequest.getName());
        validateLineColorNotDuplicated(lineRequest.getColor());

        Line line = lineRepository.save(requestAssembler.temporaryLine(lineRequest));
        return responseAssembler.lineResponse(line);
    }

    private void validateStationsExist(LineRequest lineRequest) {
        lineRequest.getSectionRequests()
                .stream()
                .flatMap(sectionDto -> Stream.of(sectionDto.getUpStationId(), sectionDto.getDownStationId()))
                .distinct()
                .forEach(integrityValidator::validateStationExist);
    }

    private void validateLineNameNotDuplicated(String name) {
        if (lineRepository.existsByName(name)) {
            throw new DuplicateLineNameException(name);
        }
    }

    private void validateLineColorNotDuplicated(String color) {
        if (lineRepository.existsByColor(color)) {
            throw new DuplicateLineColorException(color);
        }
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        List<Line> lines = lineRepository.getAll();
        return responseAssembler.lineResponses(lines);
    }

    @Transactional(readOnly = true)
    public LineResponse findById(long id) {
        Line line = lineRepository.getById(id);
        return responseAssembler.lineResponse(line);
    }

    public void update(long lineId, LineRequest lineRequest) {
        Line line = lineRepository.getById(lineId);

        validateLineNameNotDuplicated(lineRequest.getName());
        validateLineColorNotDuplicated(lineRequest.getColor());

        line.update(lineRequest.getName(), lineRequest.getColor());
        lineRepository.update(line);
    }

    public void delete(long lineId) {
        lineRepository.remove(lineId);
    }

    public void appendSection(long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.getById(lineId);
        line.appendSection(requestAssembler.temporarySection(sectionRequest));
        lineRepository.updateSections(line);
    }

    public void removeStation(long lineId, long stationId) {
        Line line = lineRepository.getById(lineId);
        line.removeStation(stationId);
        lineRepository.updateSections(line);
    }
}
