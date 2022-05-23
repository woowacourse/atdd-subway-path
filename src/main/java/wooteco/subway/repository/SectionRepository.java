package wooteco.subway.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import wooteco.subway.dao.JdbcSectionDao;
import wooteco.subway.dao.JdbcStationDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dao.entity.SectionEntity;
import wooteco.subway.dto.SectionRequest;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionRepository(JdbcTemplate jdbcTemplate) {
        this.sectionDao = new JdbcSectionDao(jdbcTemplate);
        this.stationDao = new JdbcStationDao(jdbcTemplate);
    }

    public Section save(Long lineId, SectionRequest request) {
        Station up = stationDao.findById(request.getUpStationId());
        Station down = stationDao.findById(request.getDownStationId());
        Section section = new Section(up, down, request.getDistance());
        SectionEntity saved = sectionDao.save(SectionEntity.of(section, lineId));
        return new Section(saved.getId(), section.getUp(), section.getDown(), section.getDistance());
    }

    public Sections findAll() {
        List<SectionEntity> sectionEntities = sectionDao.findAll();
        List<Station> stations = stationDao.findByIdIn(collectStationIds(sectionEntities));
        return buildSections(sectionEntities, stations);
    }

    private List<Long> collectStationIds(List<SectionEntity> sectionEntities) {
        List<Long> stationIds = new ArrayList<>();
        for (SectionEntity sectionEntity : sectionEntities) {
            stationIds.add(sectionEntity.getUpStationId());
        }
        stationIds.add(sectionEntities.get(sectionEntities.size() - 1).getDownStationId());
        return stationIds;
    }

    private Sections buildSections(List<SectionEntity> sectionEntities, List<Station> stations) {
        Map<Long, Station> allStations = new HashMap<>();
        stations.forEach(station -> allStations.put(station.getId(), station));
        List<Section> sections = sectionEntities.stream()
                .map(sectionDto -> new Section(sectionDto.getId(),
                        allStations.get(sectionDto.getUpStationId()),
                        allStations.get(sectionDto.getDownStationId()),
                        sectionDto.getDistance()))
                .collect(Collectors.toList());
        return new Sections(sections);
    }

    public Section findById(Long id) {
        SectionEntity sectionEntity = sectionDao.findById(id);
        Station up = stationDao.findById(sectionEntity.getUpStationId());
        Station down = stationDao.findById(sectionEntity.getDownStationId());
        return new Section(sectionEntity.getId(), up, down, sectionEntity.getDistance());
    }

    public void delete(Long id) {
        sectionDao.delete(id);
    }

    public void update(SectionEntity sectionEntity) {
        sectionDao.update(sectionEntity);
    }
}
