package wooteco.subway.line.dao;

import java.util.Collections;
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
            .collect(Collectors.toList());

        simpleJdbcInsert.executeBatch(batchValues.toArray(new Map[sections.size()]));
    }

    public List<Section> findAll() {
        String sql = "select S.id as section_id, S.distance as section_distance, " +
            "UST.id as up_station_id, UST.name as up_station_name, " +
            "DST.id as down_station_id, DST.name as down_station_name " +
            "from SECTION S \n" +
            "left outer join STATION UST on S.up_station_id = UST.id " +
            "left outer join STATION DST on S.down_station_id = DST.id ";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        // Map<Long, List<Map<String, Object>>> resultByLine = result.stream()
        //     .collect(Collectors.groupingBy(it -> (Long)it.get("line_id")));
        return extractSections(result);
    }

    private List<Section> extractSections(List<Map<String, Object>> result) {
        if (result.isEmpty() || result.get(0).get("SECTION_ID") == null) {
            return Collections.EMPTY_LIST;
        }
        return result.stream()
            .collect(Collectors.groupingBy(it -> it.get("SECTION_ID")))
            .entrySet()
            .stream()
            .map(it ->
                new Section(
                    (Long)it.getKey(),
                    new Station((Long)it.getValue().get(0).get("UP_STATION_ID"),
                        (String)it.getValue().get(0).get("UP_STATION_Name")),
                    new Station((Long)it.getValue().get(0).get("DOWN_STATION_ID"),
                        (String)it.getValue().get(0).get("DOWN_STATION_Name")),
                    (int)it.getValue().get(0).get("SECTION_DISTANCE")))
            .collect(Collectors.toList());
    }
}