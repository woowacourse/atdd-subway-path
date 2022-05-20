package wooteco.subway.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.section.Section;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class SectionDao {
    private static final RowMapper<Section> ACTOR_ROW_MAPPER = (resultSet, rowNum) ->
            new Section(resultSet.getLong("id"), resultSet.getLong("line_id"),
                    resultSet.getLong("up_station_id"), resultSet.getLong("down_station_id"),
                    resultSet.getInt("distance"));


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public SectionDao(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public void insert(Section section){
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(section);
        insertActor.execute(parameterSource);
    }

    public void update(Section section) {
        String sql = "update SECTION set up_station_id = :upStationId, down_station_id = :downStationId, distance = :distance where id = :id";
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(section);
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public void deleteByLineIdAndStationId(Long lineId, Long stationId) {
        String sql = "delete from SECTION where line_id = :lineId and (up_station_id = :stationId or down_station_id = :stationId)";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(Map.of("lineId", lineId, "stationId", stationId));
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public List<Section> getByLineId(Long lineId) {
        String sql = "select * from SECTION where line_id = :lineId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("lineId", lineId);
        return namedParameterJdbcTemplate.query(sql,sqlParameterSource, ACTOR_ROW_MAPPER);
    }

    public List<Section> findAll() {
        String sql = "select * from SECTION";
        return namedParameterJdbcTemplate.query(sql, ACTOR_ROW_MAPPER);
    }
}
