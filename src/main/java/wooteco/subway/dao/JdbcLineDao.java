package wooteco.subway.dao;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.Line;
import wooteco.subway.dto.LineRequest;

@Component
public class JdbcLineDao implements LineDao {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Line> lineRowMapper = (resultSet, rowNum) -> new Line(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("color"),
            resultSet.getInt("extraFare")
    );

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
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, lineRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Integer> findExtraFareByIds(List<Long> ids) {
        String inSql = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sql = String.format("select extraFare from LINE where id in (%s)", inSql);

        RowMapper<Integer> rowMapper = (rs, rowNum) -> rs.getInt("extraFare");
        return jdbcTemplate.query(sql, rowMapper, ids.toArray());
    }

    @Override
    public List<Line> findAll() {
        final String sql = "select * from LINE";
        return jdbcTemplate.query(sql, lineRowMapper);
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
