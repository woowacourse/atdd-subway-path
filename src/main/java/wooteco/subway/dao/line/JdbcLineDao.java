package wooteco.subway.dao.line;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.line.Line;

@Repository
public class JdbcLineDao implements LineDao {

    private final RowMapper<Line> lineRowMapper = (resultSet, rowNum) -> new Line(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("color"),
            resultSet.getInt("extraFare")
    );

    private final JdbcTemplate jdbcTemplate;

    public JdbcLineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long save(Line line) {
        final String sql = "insert into LINE (name, color, extraFare) values (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, line.getName());
            preparedStatement.setString(2, line.getColor());
            preparedStatement.setInt(3, line.getExtraFare());
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public boolean existLineById(Long id) {
        final String sql = "select exists (select * from LINE where id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }

    @Override
    public List<Line> findAll() {
        final String sql = "select id, name, color, extraFare from LINE";
        return jdbcTemplate.query(sql, lineRowMapper);
    }

    @Override
    public Line findById(Long id) {
        final String sql = "select id, name, color, extraFare from LINE where id = ?";
        return jdbcTemplate.queryForObject(sql, lineRowMapper, id);
    }

    @Override
    public List<Line> findByIds(List<Long> ids) {
        final String inCondition = String.join(",", Collections.nCopies(ids.size(), "?"));
        final String sql = String.format("select id, name, color, extraFare from LINE WHERE id IN (%s)", inCondition);
        return jdbcTemplate.query(sql, ids.toArray(), lineRowMapper);
    }

    @Override
    public void update(Line line) {
        final String sql = "update LINE set name = ?, color = ?, extraFare = ? where id = ?";
        jdbcTemplate.update(sql, line.getName(), line.getColor(), line.getExtraFare(), line.getId());
    }

    @Override
    public void delete(Long id) {
        final String sql = "delete from LINE where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
