package wooteco.subway.dao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.line.Line;

@Repository
public class LineDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Line> eventRowMapper = (resultSet, rowNum)
            -> new Line(resultSet.getLong("id")
            , resultSet.getString("name")
            , resultSet.getString("color")
            , resultSet.getInt("extra_fare"));

    public LineDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Line line) {
        String sql = "insert into LINE (name, color, extra_fare) values (:name, :color, :extraFare)";
        SqlParameterSource source = new BeanPropertySqlParameterSource(line);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, source, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public boolean existsByName(String name) {
        String sql = "select exists (select 1 from LINE where name = :name)";
        SqlParameterSource source = new MapSqlParameterSource("name", name);
        return Objects.requireNonNull(jdbcTemplate.queryForObject(sql, source, Boolean.class));
    }

    public boolean existsByNameExceptWithId(String name, Long id) {
        String sql = "select exists (select 1 from LINE where id != :id and name = :name)";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", id);
        source.addValue("name", name);
        return Objects.requireNonNull(jdbcTemplate.queryForObject(sql, source, Boolean.class));
    }

    public boolean existsById(Long id) {
        String sql = "select exists (select 1 from LINE where id = :id)";
        SqlParameterSource source = new MapSqlParameterSource("id", id);
        return Objects.requireNonNull(jdbcTemplate.queryForObject(sql, source, Boolean.class));
    }

    public Optional<Line> findById(Long id) {
        try {
            String sql = "select * from LINE where id = :id";
            SqlParameterSource source = new MapSqlParameterSource("id", id);
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, source, eventRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Line> findAll() {
        String sql = "select * from LINE";
        return jdbcTemplate.query(sql, eventRowMapper);
    }

    public List<Line> findByIds(List<Long> ids) {
        String sql = "select * from LINE where id in (:ids)";
        SqlParameterSource source = new MapSqlParameterSource("ids", ids);
        return jdbcTemplate.query(sql, source, eventRowMapper);
    }

    public void update(Line line) {
        String sql = "update LINE set name=:name, color=:color where id=:id";
        SqlParameterSource source = new BeanPropertySqlParameterSource(line);
        jdbcTemplate.update(sql, source);
    }

    public void deleteById(Long id) {
        String sql = "delete from LINE where id=:id";
        SqlParameterSource source = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sql, source);
    }
}
