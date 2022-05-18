package wooteco.subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Section;
import wooteco.subway.repository.dao.SectionDao;
import wooteco.subway.repository.entity.SectionEntity;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final StationRepository stations;

    public SectionRepository(SectionDao sectionDao, StationRepository stations) {
        this.sectionDao = sectionDao;
        this.stations = stations;
    }

    public void saveAll(List<Section> sections) {
        List<SectionEntity> entities = sections.stream()
                .map(SectionEntity::from)
                .collect(Collectors.toList());
        sectionDao.saveAll(entities);
    }

    public void deleteByLineId(Long lineId) {
        sectionDao.deleteByLineId(lineId);
    }

    public List<Section> findByLineId(Long lineId) {
        return sectionDao.findByLineId(lineId).stream()
                .map(this::toSection)
                .collect(Collectors.toList());
    }

    private Section toSection(SectionEntity entity) {
        return new Section(entity.getId(), entity.getLineId(),
                stations.findById(entity.getUpStationId()),
                stations.findById(entity.getDownStationId()),
                entity.getDistance());
    }

    public List<Section> findAll() {
        return sectionDao.findAll().stream()
                .map(this::toSection)
                .collect(Collectors.toList());
    }
}
