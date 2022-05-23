package wooteco.subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;
import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.repository.dao.LineDao;
import wooteco.subway.repository.entity.LineEntity;

@Repository
public class LineRepository {

    private final LineDao lineDao;

    public LineRepository(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public Line findById(Long id) {
        return lineDao.findById(id)
                .map(this::toLine)
                .orElseThrow(this::throwNotFoundException);
    }

    private NotFoundException throwNotFoundException() {
        return new NotFoundException(ExceptionMessage.NOT_FOUND_LINE.getContent());
    }

    private Line toLine(LineEntity entity) {
        return new Line(entity.getId(), entity.getName(), entity.getColor(), entity.getExtraFare());
    }

    public List<Line> findAll() {
        return lineDao.findAll().stream()
                .map(this::toLine)
                .collect(Collectors.toList());
    }

    public void update(Line line) {
        checkLineExists(line.getId());
        lineDao.update(LineEntity.from(line));
    }

    private void checkLineExists(Long id) {
        lineDao.findById(id)
                .orElseThrow(this::throwNotFoundException);
    }

    public Line save(Line line) {
        LineEntity saved = lineDao.save(LineEntity.from(line));
        return new Line(saved.getId(), saved.getName(), saved.getColor(), saved.getExtraFare());
    }

    public void deleteById(Long id) {
        checkLineExists(id);
        lineDao.deleteById(id);
    }
}
