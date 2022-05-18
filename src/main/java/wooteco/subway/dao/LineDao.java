package wooteco.subway.dao;

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
        String sql = "select * from LINE";
        return namedParameterJdbcTemplate.query(sql, ACTOR_ROW_MAPPER);
    }

    public void delete(Long id) {
        String sql = "delete from STATION where id = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public Line findById(Long id) {
        String sql = "select * from LINE where id = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, ACTOR_ROW_MAPPER);
    }

    public void edit(Line line) {
        String sql = "update LINE set name = :name, color = :color where id = :id";
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(line);
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public boolean existByName(String name) {
        String sql = "select EXISTS (select id from LINE where name = :name) as success";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("name", name);
        return Boolean.TRUE.equals(namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Boolean.class));
    }
}
