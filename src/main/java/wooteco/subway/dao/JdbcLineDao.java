package wooteco.subway.dao;

import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;
import wooteco.subway.exception.NotFoundLineException;

@Repository
public class JdbcLineDao implements LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;
    private final RowMapper<Line> lineRowMapper = (resultSet, rowNum) -> new Line(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("color"),
            resultSet.getInt("extra_fare")
    );

    public JdbcLineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("LINE")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(Line line) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(line);
        return insertActor.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<Line> findAll() {
        String sql = "SELECT * FROM LINE";
        return jdbcTemplate.query(sql, lineRowMapper);
    }

    @Override
    public void deleteById(Long lineId) {
        String sql = "DELETE FROM LINE WHERE id = (?)";
        jdbcTemplate.update(sql, lineId);
    }

    @Override
    public Line findById(Long lineId) {
        String sql = "SELECT * FROM LINE WHERE id = (?)";
        try {
            return jdbcTemplate.queryForObject(sql, lineRowMapper, lineId);
        } catch (EmptyResultDataAccessException exception) {
            throw new NotFoundLineException();
        }
    }

    @Override
    public List<Line> findByIds(List<Long> lineIds) {
        String inSql = String.join(", ", Collections.nCopies(lineIds.size(), "?"));
        String sql = String.format("SELECT * FROM LINE WHERE id IN (%s)", inSql);
        return jdbcTemplate.query(sql, lineRowMapper, lineIds.toArray());
    }

    @Override
    public void update(Long lineId, Line line) {
        String sql = "UPDATE LINE SET name = (?), color = (?), extra_fare = (?) WHERE id = (?)";
        jdbcTemplate.update(sql, line.getName(), line.getColor(), line.getExtraFare(), lineId);
    }

    @Override
    public boolean existByName(Line line) {
        String sql = "SELECT EXISTS (SELECT * FROM LINE WHERE name = (?))";
        return jdbcTemplate.queryForObject(sql, Boolean.class, line.getName());
    }

    @Override
    public boolean existByColor(Line line) {
        String sql = "SELECT EXISTS (SELECT * FROM LINE WHERE color = (?))";
        return jdbcTemplate.queryForObject(sql, Boolean.class, line.getColor());
    }

    @Override
    public boolean existByNameExceptSameId(Long lineId, Line line) {
        String sql = "SELECT EXISTS (SELECT * FROM LINE WHERE name = (?) AND NOT id = (?))";
        return jdbcTemplate.queryForObject(sql, Boolean.class, line.getName(), lineId);
    }

    @Override
    public boolean existByColorExceptSameId(Long lineId, Line line) {
        String sql = "SELECT EXISTS (SELECT * FROM LINE WHERE color = (?) AND NOT id = (?))";
        return jdbcTemplate.queryForObject(sql, Boolean.class, line.getColor(), lineId);
    }
}
