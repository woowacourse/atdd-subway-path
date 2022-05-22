package wooteco.subway.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;

@Repository
public class LineDaoImpl implements LineDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public LineDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Line line) {
        final String sql = "INSERT INTO line (name, color) VALUES (:name, :color)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final Map<String, Object> param = new HashMap<>();
        param.put("name", line.getName());
        param.put("color", line.getColor());
        jdbcTemplate.update(sql, new MapSqlParameterSource(param), keyHolder, new String[]{"id"});
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<Line> findAll() {
        final String sql = "SELECT * FROM line";
        return jdbcTemplate.query(sql, new LineMapper());
    }

    @Override
    public boolean deleteById(Long id) {
        final String sql = "DELETE FROM line where id = :id";
        final SqlParameterSource params = new MapSqlParameterSource("id", id);
        int updateSize = jdbcTemplate.update(sql, params);
        return updateSize != 0;
    }

    @Override
    public Optional<Line> findById(Long id) {
        final String sql = "SELECT * FROM line where id = :id";
        final SqlParameterSource params = new MapSqlParameterSource("id", id);
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, params, new LineMapper()));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public boolean updateById(Line line) {
        final String sql = "UPDATE line SET name = :name, color = :color where id = :id";
        final SqlParameterSource params = new BeanPropertySqlParameterSource(line);
        final int updateSize = jdbcTemplate.update(sql, params);
        return updateSize != 0;
    }

    @Override
    public boolean existsByName(String name) {
        final String sql = "SELECT EXISTS (SELECT * FROM line WHERE name = :name)";
        final SqlParameterSource params = new MapSqlParameterSource("name", name);
        return jdbcTemplate.queryForObject(sql, params, Boolean.class);
    }

    private static class LineMapper implements RowMapper<Line> {
        public Line mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new Line(rs.getLong("id"), rs.getString("name"), rs.getString("color"));
        }
    }
}
