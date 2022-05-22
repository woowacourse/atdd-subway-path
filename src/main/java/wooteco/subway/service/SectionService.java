package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.exception.NotFoundStationException;

@Service
@Transactional
public class SectionService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void createSection(Long lineId, SectionRequest sectionRequest) {
        Sections sections = sectionDao.findSectionsByLineId(lineId);

        Station upStation = stationDao.findById(sectionRequest.getUpStationId())
                .orElseThrow(NotFoundStationException::new);
        Station downStation = stationDao.findById(sectionRequest.getDownStationId())
                .orElseThrow(NotFoundStationException::new);
        Distance distance = new Distance(sectionRequest.getDistance());

        sections.addSection(new Section(upStation, downStation, distance));
        saveSections(lineId, sections);
    }

    public Sections getSectionsByLineId(Long lineId) {
        return sectionDao.findSectionsByLineId(lineId);
    }

    public void deleteStationById(Long lineId, Long stationId) {
        Sections sections = sectionDao.findSectionsByLineId(lineId);
        Station station = stationDao.findById(stationId).orElseThrow(NotFoundStationException::new);

        sections.deleteStation(station);
        saveSections(lineId, sections);
    }

    private void saveSections(Long lineId, Sections sections) {
        sectionDao.removeAllSectionsByLineId(lineId);

        for (Section section : sections.getValue()) {
            sectionDao.save(lineId, section);
        }
    }
}
