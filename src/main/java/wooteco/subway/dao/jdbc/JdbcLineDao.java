package wooteco.subway.dao.jdbc;

import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.Line;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class JdbcLineDao implements LineDao {

    private static final RowMapper<Line> LINE_ROW_MAPPER = (rs, rowNum) -> new Line(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("color"),
            rs.getInt("extra_fare")
    );

    private final JdbcTemplate jdbcTemplate;

    public JdbcLineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Line create(Line line) {
        final SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", line.getName());
        parameters.put("color", line.getColor());
        parameters.put("extra_fare", line.getExtraFare());

        final Number number = simpleJdbcInsert.executeAndReturnKey(parameters);
        return new Line(number.longValue(), line.getName(), line.getColor(), line.getExtraFare());
    }

    @Override
    public Line findById(Long id) {
        String sql = "SELECT * FROM line WHERE id=?";
        return jdbcTemplate.queryForObject(sql, LINE_ROW_MAPPER, id);
    }

    @Override
    public List<Line> findAll() {
        String sql = "SELECT * FROM line";
        return jdbcTemplate.query(sql, LINE_ROW_MAPPER);
    }

    @Override
    public void update(Long id, String name, String color, int extraFare) {
        String sql = "UPDATE line SET name=?, color=?, extra_fare=? WHERE id=?";
        jdbcTemplate.update(sql, name, color, extraFare, id);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM line WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existById(Long id) {
        String sql = "SELECT exists (select * FROM line WHERE id=?)";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != 0;
    }

    @Override
    public boolean existByName(String name) {
        String sql = "SELECT exists (select * FROM line WHERE name=?)";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != 0;
    }

    @Override
    public boolean existByColor(String color) {
        String sql = "SELECT exists (select * FROM line WHERE color=?)";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, color);
        return count != 0;
    }
}
