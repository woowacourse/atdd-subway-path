package wooteco.subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.dao.entity.LineEntity;
import wooteco.subway.dao.entity.SectionEntity;
import wooteco.subway.dao.entity.StationEntity;
import wooteco.subway.domain.element.Line;
import wooteco.subway.domain.element.Section;
import wooteco.subway.domain.element.Station;
import wooteco.subway.exception.NotFoundException;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    public SectionRepository(SectionDao sectionDao, LineDao lineDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public void save(Section section) {
        if (section.getId() == 0L) {
            sectionDao.save(toEntity(section));
            return;
        }
        sectionDao.update(toEntity(section));
    }

    public List<Section> findAll() {
        return toSections(sectionDao.findAll());
    }

    private List<Section> toSections(List<SectionEntity> entities) {
        return entities.stream()
                .map(entity -> new Section(
                        entity.getId(),
                        toLine(entity.getLineId()),
                        toStation(getStationEntity(entity.getUpStationId())),
                        toStation(getStationEntity(entity.getDownStationId())),
                        entity.getDistance()))
                .collect(Collectors.toList());
    }

    private Line toLine(Long id) {
        LineEntity entity = lineDao.findById(id)
                .orElseThrow(() -> new NotFoundException("조회하려는 id가 존재하지 않습니다. id : " + id));
        return new Line(entity.getId(), entity.getName(), entity.getColor(), entity.getExtraFare());
    }

    public List<Section> findSectionByLine(Line line) {
        List<SectionEntity> entities = sectionDao.findByLineId(line.getId());
        return toSections(line, entities);
    }

    private List<Section> toSections(Line line, List<SectionEntity> entities) {
        return entities.stream()
                .map(entity -> new Section(
                        entity.getId(),
                        line,
                        toStation(getStationEntity(entity.getUpStationId())),
                        toStation(getStationEntity(entity.getDownStationId())),
                        entity.getDistance()))
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        sectionDao.deleteById(id);
    }

    private Station toStation(StationEntity entity) {
        return new Station(entity.getId(), entity.getName());
    }

    private SectionEntity toEntity(Section section) {
        return new SectionEntity(
                section.getId(),
                section.getLine().getId(),
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                section.getDistance());
    }

    private StationEntity getStationEntity(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new NotFoundException("조회하려는 id가 존재하지 않습니다. id : " + id));
    }
}
