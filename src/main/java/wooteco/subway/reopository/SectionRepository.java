package wooteco.subway.reopository;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.reopository.dao.LineDao;
import wooteco.subway.reopository.dao.SectionDao;
import wooteco.subway.reopository.dao.StationDao;
import wooteco.subway.reopository.entity.LineEntity;
import wooteco.subway.reopository.entity.SectionEntity;
import wooteco.subway.reopository.entity.StationEntity;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public SectionRepository(SectionDao sectionDao, StationDao stationDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public Long save(Section section) {
        return sectionDao.save(SectionEntity.from(section));
    }

    public List<Section> findByLineId(Long lineId) {
        return sectionDao.findByLineId(lineId).stream()
                .map(entity -> toSection(entity))
                .collect(toList());
    }

    public List<Section> findAll() {
        return sectionDao.findAll().stream()
                .map(entity -> toSection(entity))
                .collect(toList());
    }

    public void update(Section section) {
        sectionDao.update(SectionEntity.from(section));
    }

    public void deleteById(Long id) {
        sectionDao.deleteById(id);
    }

    private Section toSection(SectionEntity entity) {
        Line line = findLine(entity);
        Station upStation = findStation(entity.getUpStationId(), "조회 하려는 상행역이 없습니다.");
        Station downStation = findStation(entity.getDownStationId(), "조회 하려는 하행역이 없습니다.");
        return new Section(entity.getId(), line, upStation, downStation, entity.getDistance());
    }

    private Station findStation(Long id, String message) {
        StationEntity stationEntity = stationDao
                .findById(id).orElseThrow(() -> new NotFoundException(message));
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    private Line findLine(SectionEntity entity) {
        LineEntity lineEntity = lineDao.findById(entity.getLineId())
                .orElseThrow(() -> new IllegalArgumentException("조회하려는 노선이 없습니다."));
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
    }
}
