package wooteco.subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Line> lineRowMapper = (resultSet, rowNum) -> new Line(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("color"),
            resultSet.getInt("extra_fare")
    );

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Line save(Line line) {
        final String sql = "insert into Line(name, color, extra_fare) values (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, line.getName());
            ps.setString(2, line.getColor());
            ps.setInt(3, line.getExtraFare());
            return ps;
        }, keyHolder);

        return new Line(
                Objects.requireNonNull(keyHolder.getKey()).longValue(),
                line.getName(),
                line.getColor(),
                line.getExtraFare()
        );
    }

    public List<Line> findAll() {
        final String sql = "select id, name, color, extra_fare from Line";
        return jdbcTemplate.query(sql, lineRowMapper);
    }

    public Line findById(Long id) {
        final String sql = "select id, name, color, extra_fare from Line where id = ?";
        return jdbcTemplate.queryForObject(sql, lineRowMapper, id);
    }

    public int update(Long id, Line line) {
        final String sql = "update Line set name = ?, color = ?, extra_fare = ? where id = ?";
        return jdbcTemplate.update(sql, line.getName(), line.getColor(), line.getExtraFare(), id);
    }

    public int deleteById(Long id) {
        final String sql = "delete from Line where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int isExistLine(Line line) {
        String sql = "select EXISTS (select name from line where name = ?) as success";
        return jdbcTemplate.queryForObject(sql, Integer.class, line.getName());
    }
}
