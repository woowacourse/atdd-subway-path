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
import wooteco.subway.dto.SectionDto;
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
        SectionDto saved = sectionDao.save(SectionDto.of(section, lineId));
        return new Section(saved.getId(), section.getUp(), section.getDown(), section.getDistance());
    }

    public Sections findAll() {
        List<SectionDto> sectionDtos = sectionDao.findAll();
        List<Station> stations = stationDao.findByIdIn(collectStationIds(sectionDtos));
        return buildSections(sectionDtos, stations);
    }

    private List<Long> collectStationIds(List<SectionDto> sectionDtos) {
        List<Long> stationIds = new ArrayList<>();
        for (SectionDto sectionDto : sectionDtos) {
            stationIds.add(sectionDto.getUpStationId());
        }
        stationIds.add(sectionDtos.get(sectionDtos.size() - 1).getDownStationId());
        return stationIds;
    }

    private Sections buildSections(List<SectionDto> sectionDtos, List<Station> stations) {
        Map<Long, Station> allStations = new HashMap<>();
        stations.forEach(station -> allStations.put(station.getId(), station));
        List<Section> sections = sectionDtos.stream()
                .map(sectionDto -> new Section(sectionDto.getId(),
                        allStations.get(sectionDto.getUpStationId()),
                        allStations.get(sectionDto.getDownStationId()),
                        sectionDto.getDistance()))
                .collect(Collectors.toList());
        return new Sections(sections);
    }

    public Section findById(Long id) {
        SectionDto sectionDto = sectionDao.findById(id);
        Station up = stationDao.findById(sectionDto.getUpStationId());
        Station down = stationDao.findById(sectionDto.getDownStationId());
        return new Section(sectionDto.getId(), up, down, sectionDto.getDistance());
    }

    public void delete(Long id) {
        sectionDao.delete(id);
    }

    public void update(SectionDto sectionDto) {
        sectionDao.update(sectionDto);
    }
}
