package wooteco.subway.line.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

@Service
public class SectionService {
    private SectionDao sectionDao;
    private StationDao stationDao;

    public SectionService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Section addInitSection(Line line, Station upStation, Station downStation, int distance) {
        if (upStation!= null && downStation != null) {
            return sectionDao.insert(line,  new Section(upStation, downStation, distance));
        }
        return null;
    }

    public void addLineStation(Line line, SectionRequest sectionRequest) {
        Station upStation = stationDao.findById(sectionRequest.getUpStationId());
        Station downStation = stationDao.findById(sectionRequest.getDownStationId());

        line.addSection(upStation, downStation, sectionRequest.getDistance());

        sectionDao.deleteByLineId(line.getId());
        sectionDao.insertSections(line);
    }

    public void removeLineStation(Line line, Station station) {
        line.removeSection(station);

        sectionDao.deleteByLineId(line.getId());
        sectionDao.insertSections(line);
    }
}
