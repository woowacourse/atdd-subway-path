package wooteco.subway.repository;

import org.springframework.stereotype.Repository;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.SectionEntity;
import wooteco.subway.domain.station.Station;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SectionRepository {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionRepository(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public Section save(SectionEntity sectionEntity) {
        SectionEntity newSectionEntity = sectionDao.insert(sectionEntity);

        return assembleSection(newSectionEntity);
    }

    public List<Section> findByLineId(Long lineId) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);

        return sectionEntities.stream()
                .map(this::assembleSection)
                .collect(Collectors.toList());
    }

    public Section assembleSection(SectionEntity sectionEntity) {
        Station upStation = stationDao.findById(sectionEntity.getUpStationId());
        Station downStation = stationDao.findById(sectionEntity.getDownStationId());

        return new Section(sectionEntity.getId(), sectionEntity.getLineId(),
                upStation, downStation, sectionEntity.getDistance());
    }

    public Station findStationById(Long stationId) {
        return stationDao.findById(stationId);
    }

    public void update(SectionEntity sectionEntity) {
        sectionDao.update(sectionEntity);
    }

    public void delete(Long lineId, Long stationId) {
        sectionDao.delete(lineId, stationId);
    }
}
