package wooteco.subway.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

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
        for (Section section : sections) {
            sectionDao.save(new Section(lineId, section));
        }
    }

    public void deleteSections(Long lineId, List<Section> sections) {
        for (Section section : sections) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            int distance = section.getDistance();
            sectionDao.delete(new Section(lineId, upStation, downStation, distance));
        }
    }
}
