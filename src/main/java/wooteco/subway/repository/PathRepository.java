package wooteco.subway.repository;

import org.springframework.stereotype.Repository;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.line.LineEntity;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.SectionEntity;
import wooteco.subway.domain.station.Station;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class PathRepository {
    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public PathRepository(StationDao stationDao, SectionDao sectionDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public List<Section> findAllSections() {
        List<SectionEntity> sectionEntities = sectionDao.findAll();

        return sectionEntities.stream()
                .map(this::assembleSection)
                .collect(Collectors.toList());
    }

    private Section assembleSection(SectionEntity sectionEntity) {
        Station upStation = stationDao.findById(sectionEntity.getUpStationId());
        Station downStation = stationDao.findById(sectionEntity.getDownStationId());

        return new Section(sectionEntity.getId(), sectionEntity.getLineId(),
                upStation, downStation, sectionEntity.getDistance());
    }

    public Station findStationById(Long stationId) {
        return stationDao.findById(stationId);
    }

    public Map<Long, Integer> getLineExtraFares() {
        return lineDao.findAll()
                .stream()
                .collect(Collectors.toMap(LineEntity::getId, LineEntity::getExtraFare));
    }
}
