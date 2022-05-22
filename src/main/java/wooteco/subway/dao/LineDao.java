package wooteco.subway.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;

@Repository
public class LineDao {

    private static final String TABLE_NAME = "LINE";
    private static final String KEY_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_NAME);
    }

    public Line save(Line line) {
        Long id = insertActor.executeAndReturnKey(generateParameter(line)).longValue();
        return findById(id).get();
    }

    private SqlParameterSource generateParameter(Line line) {
        return new MapSqlParameterSource("name", line.getName())
                .addValue("color", line.getColor())
                .addValue("extra_fare", line.getExtraFare());
    }

    public boolean existsByName(String name) {
        String sql = "select exists (select 1 from LINE where name = :name)";
        return jdbcTemplate.queryForObject(sql, Map.of("name", name), Boolean.class);
    }

    public List<Line> findAll() {
        String sql = "select * from LINE";
        return jdbcTemplate.query(sql, generateMapper());
    }

    public Optional<Line> findById(Long id) {
        String sql = "select * from LINE where id = :id";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, Map.of("id", id), generateMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Line> generateMapper() {
        return (resultSet, rowNum) ->
                new Line(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("color"),
                        resultSet.getInt("extra_fare")
                );
    }

    public void update(Long id, Line updateLine) {
        String sql = "update LINE set name = :name, color = :color where id = :id";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("name", updateLine.getName())
                .addValue("color", updateLine.getColor())
                .addValue("extra_fare", updateLine.getExtraFare());

        jdbcTemplate.update(sql, sqlParameterSource);
    }

    public void deleteById(Long id) {
        String sql = "delete from LINE where id = :id";
        jdbcTemplate.update(sql, Map.of("id", id));
    }
}
