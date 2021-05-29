package wooteco.subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

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
        Map<String, Object> params = new HashMap();
        params.put("line_id", line.getId());
        params.put("up_station_id", section.getUpStation().getId());
        params.put("down_station_id", section.getDownStation().getId());
        params.put("distance", section.getDistance());
        Long sectionId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Section(sectionId, section.getUpStation(), section.getDownStation(), section.getDistance());
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
                .collect(toList());

        simpleJdbcInsert.executeBatch(batchValues.toArray(new Map[sections.size()]));
    }

    public List<Section> findAll() {
        String query = "SELECT S.id as section_id, S.distance as distance,\n" +
                "UST.id as up_station_id, UST.name as up_station_name,\n" +
                "DST.id as down_station_id, DST.name as down_station_name,\n" +
                "FROM SECTION S\n" +
                "JOIN STATION UST on S.UP_STATION_ID = UST.id\n" +
                "JOIN STATION DST on S.DOWN_STATION_ID = DST.id";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        return mapSection(result);
    }

    private List<Section> mapSection(List<Map<String, Object>> result) {
        List<Section> collect = result.stream()
                .map(row -> {
                    Station upStation = new Station((Long) row.get("up_station_id"), (String) row.get("up_station_name"));
                    Station downStation = new Station((Long) row.get("down_station_id"), (String) row.get("down_station_name"));
                    return new Section((Long) row.get("section_id"), upStation, downStation, (Integer) row.get("distance"));
                })
                .collect(toList());
        return collect;
    }
}