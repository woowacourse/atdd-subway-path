package wooteco.subway.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.entity.LineEntity;

@Repository
public class JdbcLineRepository implements LineRepository {

    private final LineDao lineDao;
    private final SectionRepository sectionRepository;

    public JdbcLineRepository(LineDao lineDao, SectionRepository sectionRepository) {
        this.lineDao = lineDao;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public Long save(Line line) {
        return lineDao.save(line);
    }

    @Override
    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();
        return lineEntities.stream()
                .map(lineEntity -> lineEntityToLine(lineEntity,
                        sectionRepository.findSectionsByLineId(lineEntity.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Line> findById(Long id) {
        try {
            LineEntity lineEntity = lineDao.findById(id);
            Sections sections = sectionRepository.findSectionsByLineId(id);
            Line line = lineEntityToLine(lineEntity, sections);
            return Optional.of(line);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Line> findByName(String name) {
        try {
            LineEntity lineEntity = lineDao.findByName(name);
            Long lineId = lineEntity.getId();
            Sections sections = sectionRepository.findSectionsByLineId(lineId);
            Line line = lineEntityToLine(lineEntity, sections);
            return Optional.of(line);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Line lineEntityToLine(LineEntity lineEntity, Sections sections) {
        Long id = lineEntity.getId();
        String name = lineEntity.getName();
        String color = lineEntity.getColor();
        Fare extraFare = new Fare(lineEntity.getExtraFare());
        return new Line(id, name, color, extraFare, sections);
    }

    @Override
    public void update(Long id, String name, String color) {
        lineDao.update(id, name, color);
    }

    @Override
    public void deleteById(Long id) {
        lineDao.deleteById(id);
    }
}
