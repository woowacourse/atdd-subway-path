package wooteco.subway.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.section.SectionEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component
public class SectionDao {
    private static final RowMapper<SectionEntity> ACTOR_ROW_MAPPER = (resultSet, rowNum) ->
            new SectionEntity(resultSet.getLong("id"), resultSet.getLong("line_id"),
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

    public SectionEntity insert(SectionEntity sectionEntity) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(sectionEntity);
        Long id = insertActor.executeAndReturnKey(parameterSource).longValue();
        return new SectionEntity(id, sectionEntity.getLineId(), sectionEntity.getUpStationId(),
                sectionEntity.getDownStationId(), sectionEntity.getDistance());
    }

    public List<SectionEntity> findAll() {
        String sql = "select * from SECTION";
        return namedParameterJdbcTemplate.query(sql, ACTOR_ROW_MAPPER);
    }

    public List<SectionEntity> findByLineId(Long lineId) {
        String sql = "select * from SECTION where line_id = :lineId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("lineId", lineId);
        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, ACTOR_ROW_MAPPER);
    }

    public void update(SectionEntity sectionEntity) {
        String sql = "update SECTION set up_station_id = :upStationId, down_station_id = :downStationId, distance = :distance where id = :id";
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(sectionEntity);
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public void delete(Long lineId, Long stationId) {
        String sql = "delete from SECTION where line_id = :lineId and (up_station_id = :stationId or down_station_id = :stationId)";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(Map.of("lineId", lineId, "stationId", stationId));
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }
}
