package wooteco.subway.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.line.LineEntity;

import javax.sql.DataSource;
import java.util.List;

@Component
public class LineDao {
    private static final RowMapper<LineEntity> ACTOR_ROW_MAPPER = (resultSet, rowNum) ->
            new LineEntity(resultSet.getLong("id"), resultSet.getString("name"),
                    resultSet.getString("color"), resultSet.getInt("extraFare"));

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public LineDao(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public LineEntity insert(LineEntity lineInfo) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(lineInfo);
        Long id = insertActor.executeAndReturnKey(parameterSource).longValue();
        return new LineEntity(id, lineInfo.getName(), lineInfo.getColor(), lineInfo.getExtraFare());
    }

    public List<LineEntity> findAll() {
        String sql = "select * from LINE";
        return namedParameterJdbcTemplate.query(sql, ACTOR_ROW_MAPPER);
    }

    public LineEntity findById(Long id) {
        String sql = "select * from LINE where id = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, ACTOR_ROW_MAPPER);
    }

    public void update(LineEntity lineInfo) {
        String sql = "update LINE set name = :name, color = :color, extraFare = :extraFare where id = :id";
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(lineInfo);
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public void delete(Long id) {
        String sql = "delete from LINE where id = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public boolean existByName(String name) {
        String sql = "select EXISTS (select id from LINE where name = :name) as success";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("name", name);
        return Boolean.TRUE.equals(namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Boolean.class));
    }
}
