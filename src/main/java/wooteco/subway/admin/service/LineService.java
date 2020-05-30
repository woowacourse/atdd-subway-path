package wooteco.subway.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.dto.request.LineRequest;
import wooteco.subway.admin.dto.response.LineResponse;
import wooteco.subway.admin.exception.EntityNotFoundException;
import wooteco.subway.admin.repository.LineRepository;

@Service
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public List<LineResponse> showLines() {
        return LineResponse.listOf(lineRepository.findAll());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long id) {
        return LineResponse.of(findBy(id));
    }

    @Transactional
    public Line save(Line line) {
        return lineRepository.save(line);
    }

    @Transactional
    public void updateLine(Long id, LineRequest request) {
        Line persistLine = findBy(id);
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private Line findBy(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Line.class.getName(), id));
    }
}
