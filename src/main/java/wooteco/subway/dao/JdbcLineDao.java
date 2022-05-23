package wooteco.subway.dao;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;

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

    public JdbcLineDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("LINE")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(final Line line) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(line);
        return insertActor.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<Line> findAll() {
        final String sql = "SELECT * FROM LINE";
        return jdbcTemplate.query(sql, lineRowMapper);
    }

    @Override
    public void deleteById(final Long lineId) {
        final String sql = "DELETE FROM LINE WHERE id = (?)";
        jdbcTemplate.update(sql, lineId);
    }

    @Override
    public Optional<Line> findById(final Long lineId) {
        final String sql = "SELECT * FROM LINE WHERE id = (?)";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, lineRowMapper, lineId));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public void update(final Long lineId, final Line line) {
        final String sql = "UPDATE LINE SET name = (?), color = (?), extra_fare = (?) WHERE id = (?)";
        jdbcTemplate.update(sql, line.getName(), line.getColor(), line.getExtraFare(), lineId);
    }

    @Override
    public boolean existByName(final Line line) {
        final String sql = "SELECT EXISTS (SELECT * FROM LINE WHERE name = (?) LIMIT 1)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, line.getName()));
    }

    @Override
    public boolean existByColor(final Line line) {
        final String sql = "SELECT EXISTS (SELECT * FROM LINE WHERE color = (?) LIMIT 1)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, line.getColor()));
    }

    @Override
    public boolean existByNameExceptSameId(final Long lineId, final Line line) {
        final String sql = "SELECT EXISTS (SELECT * FROM LINE WHERE name = (?) AND NOT id = (?) LIMIT 1)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, line.getName(), lineId));
    }

    @Override
    public boolean existByColorExceptSameId(final Long lineId, final Line line) {
        final String sql = "SELECT EXISTS (SELECT * FROM LINE WHERE color = (?) AND NOT id = (?) LIMIT 1)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, line.getColor(), lineId));
    }
}
