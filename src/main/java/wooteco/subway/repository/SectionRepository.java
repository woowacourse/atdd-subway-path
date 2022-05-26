package wooteco.subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.section.Section;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepository(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public List<Section> findAllSections() {
        return sectionDao.findAll();
    }

    public List<Section> findAllSectionsByLineId(Long lineId) {
        return sectionDao.findAllByLineId(lineId);
    }

    public List<Section> findAllSectionsByStationId(Long stationId) {
        return sectionDao.findAllByStationId(stationId);
    }

    public void saveSections(Long lineId, List<Section> sections) {
        List<Section> newSections = sections.stream()
                .map(section -> new Section(lineId, section))
                .collect(Collectors.toList());
        sectionDao.save(newSections);
    }

    public void deleteSections(Long lineId, List<Section> sections) {
        List<Section> oldSections = sections.stream()
                .map(section -> new Section(lineId, section))
                .collect(Collectors.toList());
        sectionDao.delete(oldSections);
    }
}
