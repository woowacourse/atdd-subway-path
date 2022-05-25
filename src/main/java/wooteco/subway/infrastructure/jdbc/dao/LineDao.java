package wooteco.subway.infrastructure.jdbc.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import wooteco.subway.infrastructure.jdbc.dao.entity.LineEntity;

@Component
public class LineDao {

    private static final RowMapper<LineEntity> ROW_MAPPER =
            (resultSet, rowNum) -> new LineEntity(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("color"),
                    resultSet.getLong("extraFare")
            );

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public LineDao(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("Line")
                .usingGeneratedKeyColumns("id");
    }

    public long save(LineEntity lineEntity) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(lineEntity);
        return jdbcInsert.executeAndReturnKey(parameters)
                .longValue();
    }

    public List<LineEntity> findAll() {
        String query = "SELECT id, name, color, extraFare from Line";
        return jdbcTemplate.query(query, ROW_MAPPER);
    }

    public Optional<LineEntity> findById(long id) {
        String query = "SELECT id, name, color, extraFare from Line WHERE id=(:id)";
        try {
            SqlParameterSource parameters = new MapSqlParameterSource("id", id);
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, parameters, ROW_MAPPER));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean existsById(long id) {
        String query = "SELECT EXISTS(SELECT id FROM Line WHERE id=(:id)) as existable";
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, parameters,
                (resultSet, rowNum) -> resultSet.getBoolean("existable")));
    }

    public boolean existsByName(String name) {
        String query = "SELECT EXISTS(SELECT id FROM Line WHERE name=(:name)) as existable";
        SqlParameterSource parameters = new MapSqlParameterSource("name", name);
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, parameters,
                (resultSet, rowNum) -> resultSet.getBoolean("existable")));
    }

    public boolean existsByColor(String color) {
        String query = "SELECT EXISTS(SELECT id FROM Line WHERE color=(:color)) as existable";
        SqlParameterSource parameters = new MapSqlParameterSource("color", color);
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, parameters,
                (resultSet, rowNum) -> resultSet.getBoolean("existable")));
    }

    public void update(LineEntity lineEntity) {
        String query = "UPDATE Line SET name=(:name), color=(:color), extraFare=(:extraFare) WHERE id=(:id)";
        SqlParameterSource parameters = new MapSqlParameterSource("id", lineEntity.getId())
                .addValue("name", lineEntity.getName())
                .addValue("color", lineEntity.getColor())
                .addValue("extraFare", lineEntity.getExtraFare());
        jdbcTemplate.update(query, parameters);
    }

    public void remove(long id) {
        String query = "DELETE FROM Line WHERE id=(:id)";
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(query, parameters);
    }
}
