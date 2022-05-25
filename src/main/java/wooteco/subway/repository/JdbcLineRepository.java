package wooteco.subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;
import wooteco.subway.repository.dao.LineDao;
import wooteco.subway.repository.entity.LineEntity;

@Repository
public class JdbcLineRepository implements LineRepository {

    private final LineDao lineDao;

    public JdbcLineRepository(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Override
    public Long save(Line line) {
        return lineDao.save(line);
    }

    @Override
    public List<Line> findAll() {
        return lineDao.findAll()
            .stream()
            .map(this::toLine)
            .collect(Collectors.toList());
    }

    private Line toLine(LineEntity line) {
        return new Line(line.getId(), line.getName(), line.getColor(), line.getExtraFare());
    }

    @Override
    public boolean deleteById(Long id) {
        return lineDao.deleteById(id);
    }

    @Override
    public Line findById(Long id) {
        LineEntity line = lineDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Id에 해당하는 노선이 존재하지 않습니다."));
        return toLine(line);
    }

    @Override
    public boolean updateById(Line line) {
        return lineDao.updateById(line);
    }

    @Override
    public boolean existsByName(String name) {
        return lineDao.existsByName(name);
    }
}
