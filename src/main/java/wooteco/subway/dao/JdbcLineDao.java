package wooteco.subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;
import wooteco.subway.dto.LineRequest;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcLineDao implements LineDao {

    private static final RowMapper<Line> LINE_ROW_MAPPER = (resultSet, rowNum) -> new Line(
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
    public Long save(LineRequest lineRequest) {
        final String sql = "insert into LINE (name, color, extraFare) values (?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, lineRequest.getName());
            ps.setString(2, lineRequest.getColor());
            ps.setInt(3, lineRequest.getExtraFare());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public Optional<Line> findById(Long id) {
        final String sql = "select * from LINE where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, LINE_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Line> findAll() {
        final String sql = "select * from LINE";
        return jdbcTemplate.query(sql, LINE_ROW_MAPPER);
    }

    @Override
    public boolean hasLine(String name) {
        final String sql = "select exists (select * from LINE where name = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, name);
    }

    @Override
    public void updateById(Long id, String name, String color, int extraFare) {
        final String sql = "update LINE set name = ?, color = ?, extraFare = ? where id = ?";
        jdbcTemplate.update(sql, name, color, extraFare, id);
    }

    @Override
    public void deleteById(Long id) {
        final String sql = "delete from LINE where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
