package wooteco.subway.line.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import wooteco.subway.station.domain.Station;

@Repository
public class SectionDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    private static final RowMapper<Section> mapper = (rs, rowNum) -> {
        Long sectionId = rs.getLong("section_id");
        Long upStationId = rs.getLong("up_id");
        String upStationName = rs.getString("up_name");
        Long downStationId = rs.getLong("down_id");
        String downStationName = rs.getString("down_name");
        int distance = rs.getInt("distance");
        return new Section(
            sectionId,
            new Station(upStationId, upStationName),
            new Station(downStationId, downStationName),
            distance
        );
    };

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
        String sql = "SELECT \n"
            + "  s.id AS section_id,\n"
            + "  up_table.id AS up_id,\n"
            + "  up_table.name AS up_name,\n"
            + "  down_table.id AS down_id,\n"
            + "  down_table.name AS down_name,\n"
            + "  distance\n"
            + "FROM\n"
            + "  section AS s\n"
            + "LEFT JOIN station AS up_table      ON s.up_station_id = up_table.id\n"
            + "LEFT JOIN station AS down_table ON s.down_station_id = down_table.id\n";
        return jdbcTemplate.query(sql, mapper);
    }
}