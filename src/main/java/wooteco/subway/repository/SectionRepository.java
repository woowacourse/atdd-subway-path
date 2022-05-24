package wooteco.subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.section.Section;
import wooteco.subway.entity.SectionEntity;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepository(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public List<Section> findAllSections() {
        return sectionDao.findAll()
                .stream()
                .map(SectionEntity::toDomain)
                .collect(Collectors.toList());
    }

    public List<Section> findAllSectionsByLineId(Long lineId) {
        return sectionDao.findAllByLineId(lineId)
                .stream()
                .map(SectionEntity::toDomain)
                .collect(Collectors.toList());
    }

    public List<Section> findAllSectionsByStationId(Long stationId) {
        return sectionDao.findAllByStationId(stationId)
                .stream()
                .map(SectionEntity::toDomain)
                .collect(Collectors.toList());
    }

    public void saveSections(Long lineId, List<Section> sections) {
        for (Section section : sections) {
            sectionDao.save(SectionEntity.of(lineId, section));
        }
    }

    public void deleteSections(Long lineId, List<Section> sections) {
        for (Section section : sections) {
            sectionDao.delete(SectionEntity.of(lineId, section));
        }
    }
}
