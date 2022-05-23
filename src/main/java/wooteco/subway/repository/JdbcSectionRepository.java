package wooteco.subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.repository.dao.LineDao;
import wooteco.subway.repository.dao.SectionDao;
import wooteco.subway.repository.entity.LineEntity;
import wooteco.subway.repository.entity.SectionEntity;

@Repository
public class JdbcSectionRepository implements SectionRepository {

    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public JdbcSectionRepository(SectionDao sectionDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    @Override
    public Long save(Section section) {
        return sectionDao.save(section);
    }

    @Override
    public List<Section> findByLineId(Long lineId) {
        List<Section> collect = sectionDao.findByLineId(lineId).stream()
            .map(this::toSection)
            .collect(Collectors.toList());
        return collect;
    }

    @Override
    public boolean update(Long sectionId, Long downStationId, int distance) {
        return sectionDao.update(sectionId, downStationId, distance);
    }

    @Override
    public boolean deleteById(Long sectionId) {
        return sectionDao.deleteById(sectionId);
    }

    @Override
    public List<Section> findAll() {
        return sectionDao.findAll().stream()
            .map(this::toSection)
            .collect(Collectors.toList());
    }

    private Section toSection(SectionEntity section) {
        LineEntity line = lineDao.findById(section.getLineId())
            .orElseThrow(() -> new IllegalArgumentException("Id에 해당하는 노선이 존재하지 않습니다."));

        return new Section(section.getId(), toLine(line), section.getUpStationId(),
            section.getDownStationId(), section.getDistance());
    }

    @Override
    public void updateAll(List<Section> insertSections) {
        sectionDao.deleteAll();
        sectionDao.insertAll(insertSections);
    }

    private Line toLine(LineEntity line) {
        return new Line(line.getId(), line.getName(), line.getColor(), line.getExtraFare());
    }
}
