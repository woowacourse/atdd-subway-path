package wooteco.subway.service;

import java.util.List;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Line;
import wooteco.subway.exception.DomainException;
import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.service.dto.LineRequest;
import wooteco.subway.service.dto.LineUpdateRequest;
import wooteco.subway.service.dto.SectionSaveRequest;

@Service
@Transactional
public class LineService {

    private final SectionService sectionService;
    private final LineRepository lineRepository;

    public LineService(SectionService sectionService, LineRepository lineRepository) {
        this.sectionService = sectionService;
        this.lineRepository = lineRepository;
    }

    public Line save(final LineRequest request) {
        try {
            Line line = Line.withoutIdOf(request.getName(), request.getColor(), request.getExtraFare());
            Line saved = lineRepository.save(line);
            sectionService.save(new SectionSaveRequest(saved.getId(), request.getUpStationId(),
                    request.getDownStationId(), request.getDistance()));
            return saved;
        } catch (DuplicateKeyException e) {
            throw new DomainException(ExceptionMessage.DUPLICATED_LINE_NAME.getContent());
        }
    }

    @Transactional(readOnly = true)
    public List<Line> findAll() {
        return lineRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Line findById(Long id) {
        return lineRepository.findById(id);
    }

    public void updateById(final Long id, final LineUpdateRequest request) {
        Line updated = new Line(id, request.getName(), request.getColor(), request.getExtraFare());
        lineRepository.update(updated);
    }

    public void deleteById(final Long id) {
        lineRepository.deleteById(id);
    }
}
