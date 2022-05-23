package wooteco.subway.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Section;

@Repository
public class SectionDaoImpl implements SectionDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SectionDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Section section) {
        final String sql = "INSERT INTO section (line_id, up_station_id, down_station_id, distance) VALUES (:lineId, :upStationId, :downStationId, :distance)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("lineId", section.getLineId());
        params.addValue("upStationId", section.getUpStationId());
        params.addValue("downStationId", section.getDownStationId());
        params.addValue("distance", section.getDistance());
        jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<Section> findByLineId(Long lineId) {
        final String sql = "SELECT * FROM section WHERE line_id = :lineId";
        SqlParameterSource params = new MapSqlParameterSource("lineId", lineId);
        return jdbcTemplate.query(sql, params, new SectionMapper());
    }

    @Override
    public boolean update(Long sectionId, Long downStationId, int distance) {
        final String sql = "UPDATE section SET down_station_id = :downStationId, distance = :distance where id = :id";
        final Map<String, Object> params = new HashMap<>();
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        params.put("id", sectionId);
        final int updateSize = jdbcTemplate.update(sql, params);
        return updateSize != 0;
    }

    @Override
    public boolean deleteById(Long sectionId) {
        final String sql = "DELETE FROM section where id = :id";
        final MapSqlParameterSource params = new MapSqlParameterSource("id", sectionId);
        int updateSize = jdbcTemplate.update(sql, params);
        return updateSize != 0;
    }

    @Override
    public List<Section> findAll() {
        final String sql = "SELECT * FROM section";
        return jdbcTemplate.query(sql, new SectionMapper());
    }

    private static class SectionMapper implements RowMapper<Section> {
        public Section mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new Section(rs.getLong("id"), rs.getLong("line_id"), rs.getLong("up_station_id"), rs.getLong("down_station_id"), rs.getInt("distance"));
        }
    }
}
