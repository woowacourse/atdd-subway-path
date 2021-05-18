package wooteco.subway.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

@Repository
public class SectionDao {

    private SimpleJdbcInsert simpleJdbcInsert;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private RowMapper<Section> rowMapper = (rs, rowNum) ->
        new Section(
            rs.getLong("id"),
            new Station(rs.getLong("up_station_id")),
            new Station(rs.getLong("down_station_id")),
            rs.getInt("distance")
        );

    public SectionDao(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("SECTION")
            .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Section> findAll() {
        String sql = "select * from SECTION";
        return namedParameterJdbcTemplate.query(sql, rowMapper);
    }

    public Section insert(Line line, Section section) {
        Map<String, Object> params = new HashMap();
        params.put("line_id", line.getId());
        params.put("up_station_id", section.getUpStation().getId());
        params.put("down_station_id", section.getDownStation().getId());
        params.put("distance", section.getDistance());
        Long sectionId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Section(sectionId, section.getUpStation(), section.getDownStation(),
            section.getDistance());
    }

    public void deleteByLineId(Long lineId) {
        Map<String, Long> params = Collections.singletonMap("line_id", lineId);
        namedParameterJdbcTemplate.update("delete from SECTION where line_id = :line_id", params);
    }

    public void insertSections(Line line) {
        List<Section> sections = line.getSections().toList();
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
}