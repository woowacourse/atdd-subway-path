package wooteco.subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.entity.LineEntity;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Sections;

@Repository
public class LineRepository {

    private final LineDao lineDao;

    private final SectionRepository sectionRepository;

    public LineRepository(final LineDao lineDao, final SectionRepository sectionRepository) {
        this.lineDao = lineDao;
        this.sectionRepository = sectionRepository;
    }

    public Long save(final Line line) {
        return lineDao.save(LineEntity.from(line));
    }

    public List<Line> findAll() {
        final List<LineEntity> entities = lineDao.findAll();
        return entities.stream()
                .map(e -> {
                    final Sections sections = sectionRepository.findAllByLineId(e.getId());
                    return e.toLine(sections);
                }).collect(Collectors.toList());
    }

    public Line find(final Long id) {
        final LineEntity entity = lineDao.findById(id);
        final Sections sections = sectionRepository.findAllByLineId(id);
        return entity.toLine(sections);
    }

    public void updateById(final Line line) {
        lineDao.updateById(LineEntity.from(line));
    }

    public void deleteById(final Long id) {
        lineDao.deleteById(id);
    }

    public boolean existsById(final Long id) {
        return lineDao.existsById(id);
    }
}
