package wooteco.subway.ui.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.SectionRequest;

@Service
public class SectionService {
    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    public SectionService(SectionDao sectionDao, LineDao lineDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public void add(SectionRequest sectionRequest, Long lineId) {
        Line line = lineDao.findById(lineId);
        Station upStation = findStationById(sectionRequest.getUpStationId());
        Station downStation = findStationById(sectionRequest.getDownStationId());
        Section section = new Section(upStation, downStation, sectionRequest.getDistance());

        line.addSection(section);
        sectionDao.save(line.getSections(), line.getId());
    }

    @Transactional
    public void delete(Long lineId, Long stationId) {
        Line line = lineDao.findById(lineId);
        Station station = findStationById(stationId);
        sectionDao.delete(line.delete(station));
        sectionDao.save(line.getSections(), line.getId());
    }

    public void deleteByLine(Long id) {
        sectionDao.deleteByLine(id);
    }

    private Station findStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 역을 찾을 수 없습니다."));
    }
}
