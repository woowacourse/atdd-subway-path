package wooteco.subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;

@Repository
public class LineDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public LineDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Line save(Line line) {
        String sql = "INSERT INTO line (name, color, extra_fare) VALUES (:name, :color, :extra_fare)";

        Map<String, Object> params = new HashMap<>();
        params.put("name", line.getName());
        params.put("color", line.getColor());
        params.put("extra_fare", line.getExtraFare());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder);

        long lineId = keyHolder.getKey().longValue();

        return new Line(lineId, line.getName(), line.getColor(), line.getExtraFare());
    }

    public List<Line> findAll() {
        String sql = "SELECT * FROM line";

        return jdbcTemplate.query(sql, getRowMapper());
    }

    public Line findById(Long id) {
        String sql = "SELECT id, name, color, extra_fare FROM line WHERE id=:id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        return jdbcTemplate.queryForObject(sql, params, getRowMapper());
    }

    public void updateById(Long id, Line line) {
        String sql = "UPDATE line SET name=:name, color=:color, extra_fare=:extra_fare where id=:id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("name", line.getName());
        params.put("color", line.getColor());
        params.put("extra_fare", line.getExtraFare());

        jdbcTemplate.update(sql, params);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM line WHERE id=:id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        jdbcTemplate.update(sql, params);
    }

    private RowMapper<Line> getRowMapper() {
        return (rs, rowNum) -> new Line(rs.getLong("id"), rs.getString("name"),
                rs.getString("color"), rs.getInt("extra_fare"));
    }
}
