package wooteco.subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.line.Line;

import javax.sql.DataSource;
import java.util.List;
import wooteco.subway.exception.line.LineNotFoundException;

@Repository
public class LineDao {
    private static final RowMapper<Line> ACTOR_ROW_MAPPER = (resultSet, rowNum) ->
            new Line(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getString("color"));

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public LineDao(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public Line insert(Line line) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(line);
        Long id = insertActor.executeAndReturnKey(parameterSource).longValue();
        return new Line(id, line.getName(), line.getColor());
    }

    public List<Line> findAll() {
        String sql = "select id, name, color from LINE";
        return namedParameterJdbcTemplate.query(sql, ACTOR_ROW_MAPPER);
    }

    public void deleteById(Long id) {
        String sql = "delete from LINE where id = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public Line getById(Long id) {
        try {
            String sql = "select id, name, color from LINE where id = :id";
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
            return namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, ACTOR_ROW_MAPPER);
        } catch(EmptyResultDataAccessException e) {
            throw new LineNotFoundException();
        }
    }

    public void edit(Line line) {
        String sql = "update LINE set name = :name, color = :color where id = :id";
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(line);
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public boolean existByNameOrColor(Line line) {
        String sql = "select EXISTS (select id from LINE where name = :name or color = :color) as success";
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(line);
        return Boolean.TRUE.equals(namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Boolean.class));
    }
}
