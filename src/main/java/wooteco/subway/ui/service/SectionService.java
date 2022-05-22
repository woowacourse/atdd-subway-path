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
        Line line = lineDao.findById(lineId).orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 노선이 존재하지 않습니다."));
        Station upStation = stationDao.findById(sectionRequest.getUpStationId())
                .orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 역이 존재하지 않습니다."));
        Station downStation = stationDao.findById(sectionRequest.getDownStationId())
                .orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 역이 존재하지 않습니다."));
        Section section = new Section(upStation, downStation, sectionRequest.getDistance());

        line.addSection(section);
        sectionDao.saveAll(line.getSections(), line.getId());
    }

    @Transactional
    public void delete(Long lineId, Long stationId) {
        Line line = lineDao.findById(lineId).orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 노선이 존재하지 않습니다."));
        Station station = stationDao.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 역이 존재하지 않습니다."));
        sectionDao.deleteById(line.delete(station));
        sectionDao.saveAll(line.getSections(), line.getId());
    }

    public void deleteByLine(Long id) {
        sectionDao.deleteByLine(id);
    }
}
