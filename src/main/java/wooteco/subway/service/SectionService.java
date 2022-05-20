package wooteco.subway.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.SectionRequest;

@Service
@Transactional
public class SectionService {
    private final SectionDao sectionDao;
    private final StationService stationService;

    public SectionService(SectionDao sectionDao, StationService stationService) {
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    public void save(SectionRequest sectionRequest, Long lineId) {
        Section section = createSection(sectionRequest);
        sectionDao.save(section, lineId);
    }

    @Transactional(readOnly = true)
    public Section createSection(SectionRequest sectionRequest) {
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());
        return new Section(upStation, downStation, sectionRequest.getDistance());
    }

    public void update(List<Section> sections) {
        sectionDao.update(sections);
    }

    public void deleteById(Long sectionId) {
        sectionDao.delete(sectionId);
    }
}
