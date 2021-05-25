package wooteco.subway.line.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

@Repository
public class SectionDao {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("SECTION")
            .usingGeneratedKeyColumns("id");
    }

    public Section insert(Line line, Section section) {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", line.getId());
        params.put("up_station_id", section.getUpStation().getId());
        params.put("down_station_id", section.getDownStation().getId());
        params.put("distance", section.getDistance());
        Long sectionId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Section(sectionId, section.getUpStation(), section.getDownStation(),
            section.getDistance());
    }

    public void deleteByLineId(Long lineId) {
        jdbcTemplate.update("delete from SECTION where line_id = ?", lineId);
    }

    public void insertSections(Line line) {
        List<Section> sections = line.getSections().getSections();
        List<Map<String, Object>> batchValues = sections.stream()
            .map(section -> {
                Map<String, Object> params = new HashMap<>();
                params.put("line_id", line.getId());
                params.put("up_station_id", section.getUpStation().getId());
                params.put("down_station_id", section.getDownStation().getId());
                params.put("distance", section.getDistance());
                return params;
            })
            .collect(Collectors.toList());

        simpleJdbcInsert.executeBatch(batchValues.toArray(new Map[sections.size()]));
    }

    public List<Section> findAll() {
        String sql = "select  S.id as sectionId, S.up_station_id as upStationId, "
            + "UST.name as upStationName, S.down_station_id as downStationId, "
            + "DST.name as downStationName, S.distance as distance "
            + "from SECTION as S "
            + "left outer join STATION as UST on S.up_station_id = UST.id "
            + "left outer join STATION as DST on S.down_station_id = UST.id";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        return toSection(result);
    }

    private List<Section> toSection(List<Map<String, Object>> result) {
        return result.stream()
            .map(item -> {
                Station upStation = getUpStation(item);
                Station downStation = getDownStation(item);
                Long id = (Long) item.get("sectionId");
                int distance = (int) item.get("distance");
                return new Section(id, upStation, downStation, distance);
            })
            .collect(Collectors.toList());
    }

    private Station getUpStation(Map<String, Object> item) {
        Long id = (Long) item.get("upStationId");
        String name = (String) item.get("upStationName");
        return new Station(id, name);
    }

    private Station getDownStation(Map<String, Object> item) {
        Long id = (Long) item.get("downStationId");
        String name = (String) item.get("downStationName");
        return new Station(id, name);
    }


}