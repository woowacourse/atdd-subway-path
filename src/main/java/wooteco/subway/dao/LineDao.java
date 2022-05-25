package wooteco.subway.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.line.Line;
import wooteco.subway.entity.LineEntity;

@Repository
public class LineDao {

    private static final RowMapper<LineEntity> LINE_ROW_MAPPER = (resultSet, rowNum) -> new LineEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("color"),
            resultSet.getInt("extra_fare")
    );

    private final JdbcTemplate jdbcTemplate;

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Line line) {
        String sql = "INSERT INTO LINE (name, color, extra_fare) VALUES (?, ?, ?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, line.getName());
            ps.setString(2, line.getColor());
            ps.setInt(3, line.getExtraFare().getValue());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<LineEntity> findAll() {
        String sql = "SELECT id, name, color, extra_fare FROM LINE";
        return jdbcTemplate.query(sql, LINE_ROW_MAPPER);
    }

    public LineEntity findById(Long id) {
        String sql = "SELECT id, name, color, extra_fare FROM LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, LINE_ROW_MAPPER, id);
    }

    public LineEntity findByName(String name) {
        String sql = "SELECT id, name, color, extra_fare FROM LINE WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, LINE_ROW_MAPPER, name);
    }

    public void update(Long id, String name, String color) {
        String sql = "UPDATE LINE SET name = ?, color = ? WHERE id = ?";
        jdbcTemplate.update(sql, name, color, id);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM LINE WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
