package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.CommonLineDao;
import wooteco.subway.domain.Line;
import wooteco.subway.dto.request.LineSaveRequest;
import wooteco.subway.dto.request.LineUpdateRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.service.dto.LineDto;

@Service
public class LineService {

    private static final int NO_ROW_AFFECTED = 0;
    private static final String LINE_NOT_FOUND = "요청한 노선이 존재하지 않습니다. ";

    private final CommonLineDao lineDao;
    private final LineRepository lineRepository;

    public LineService(final CommonLineDao lineDao, final LineRepository lineRepository) {
        this.lineDao = lineDao;
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(final LineSaveRequest lineSaveRequest) {
        final LineDto lineDto = new LineDto(lineSaveRequest.getName(), lineSaveRequest.getColor(),
                lineSaveRequest.getUpStationId(), lineSaveRequest.getDownStationId(), lineSaveRequest.getDistance());
        final Line line = lineRepository.save(lineDto);
        return LineResponse.from(line);
    }

    public LineResponse findById(final Long id) {
        return LineResponse.from(lineRepository.findById(id));
    }

    public List<Line> findAll() {
        return lineRepository.findAll();
    }

    public void updateLine(final Long id, final LineUpdateRequest lineUpdateRequest) {
        final Line line = new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
        final int theNumberOfAffectedRow = lineDao.update(id, line);
        if (theNumberOfAffectedRow == NO_ROW_AFFECTED) {
            throw new IllegalStateException(LINE_NOT_FOUND + "id=" + id + " " + line);
        }
    }

    public void deleteById(final Long id) {
        final int theNumberOfAffectedRow = lineDao.deleteById(id);
        if (theNumberOfAffectedRow == NO_ROW_AFFECTED) {
            throw new IllegalStateException(LINE_NOT_FOUND + "id=" + id);
        }
    }
}
