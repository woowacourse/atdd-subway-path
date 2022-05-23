package wooteco.subway.repository;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dao.entity.LineEntity;
import wooteco.subway.dao.entity.SectionEntity;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineRepository(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Line save(Line line) {
        LineEntity lineEntity = lineDao.save(LineEntity.from(line));
        List<SectionEntity> sectionEntities = line.getSections().stream()
                .map(section -> SectionEntity.of(section, lineEntity.getId()))
                .collect(Collectors.toList());
        sectionDao.saveAll(sectionEntities);
        return toLine(lineEntity, sectionEntities);
    }

    public Line findById(Long id) {
        LineEntity lineEntity = lineDao.findById(id);
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(id);
        return toLine(lineEntity, sectionEntities);
    }

    public List<Line> findAll() {
        return lineDao.findAll().stream()
                .map(lineDto -> findById(lineDto.getId()))
                .collect(Collectors.toList());
    }

    public void update(Line line) {
        lineDao.update(LineEntity.from(line));
    }

    public void delete(Long id) {
        lineDao.deleteById(id);
    }

    private Line toLine(LineEntity lineEntity, List<SectionEntity> sectionEntities) {
        List<Station> stations = stationDao.findByIdIn(collectStationIds(sectionEntities));
        Sections sections = toSections(sectionEntities, stations);
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sections);
    }

    private Sections toSections(List<SectionEntity> sectionEntities, List<Station> stations) {
        Map<Long, Station> allStations = stations.stream()
                .collect(Collectors.toMap(Station::getId, station -> station));

        List<Section> sections = sectionEntities.stream()
                .map(sectionDto -> new Section(sectionDto.getId(),
                        allStations.get(sectionDto.getUpStationId()),
                        allStations.get(sectionDto.getDownStationId()),
                        sectionDto.getDistance()))
                .collect(Collectors.toList());
        return new Sections(sections);
    }

    private List<Long> collectStationIds(List<SectionEntity> sectionEntities) {
        List<Long> stationIds = new ArrayList<>();
        for (SectionEntity sectionEntity : sectionEntities) {
            stationIds.add(sectionEntity.getUpStationId());
        }
        stationIds.add(sectionEntities.get(sectionEntities.size() - 1).getDownStationId());
        return stationIds;
    }
}
