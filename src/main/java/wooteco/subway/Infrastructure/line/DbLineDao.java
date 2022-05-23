package wooteco.subway.Infrastructure.line;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DbLineDao implements LineDao {

    private static final RowMapper<Line> ROW_MAPPER = (rs, rn) -> {
        long newId = rs.getLong("id");
        String name = rs.getString("name");
        String color = rs.getString("color");
        int extraFare = rs.getInt("extra_fare");
        return new Line(newId, name, color, extraFare);
    };

    private final JdbcTemplate jdbcTemplate;

    public DbLineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long save(Line line) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO LINE(name, color, extra_fare) VALUES(?, ?, ?)", new String[]{"id"});
            ps.setString(1, line.getName());
            ps.setString(2, line.getColor());
            ps.setInt(3, line.getExtraFare());
            return ps;
        }, keyHolder);

        long savedLineId = keyHolder.getKey().longValue();
        return savedLineId;
    }

    @Override
    public List<Line> findAll() {
        return jdbcTemplate.query("SELECT * FROM LINE", ROW_MAPPER);
    }

    @Override
    public Optional<Line> findById(Long id) {
        try {
            Line line = jdbcTemplate
                    .queryForObject("SELECT * FROM LINE WHERE id = ? ", ROW_MAPPER, id);
            return Optional.of(line);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Line> findByIdIn(Collection<Long> lineIds) {
        String idsString = lineIds.stream()
                .map(it -> String.valueOf(it))
                .collect(Collectors.joining(", "));
        String selectInClauseQuery = String.format("SELECT * FROM LINE WHERE id IN (%s)", idsString);

        return jdbcTemplate.query(selectInClauseQuery, ROW_MAPPER);
    }

    @Override
    public boolean existById(Long id) {
        return findById(id).isPresent();
    }

    @Override
    public boolean existByName(String name) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT id FROM LINE WHERE name = ? LIMIT 1 ) AS `exists`",
                Boolean.class, name);
    }

    @Override
    public boolean existByColor(String color) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT color FROM LINE WHERE color = ? LIMIT 1 ) AS `exists`",
                Boolean.class, color);
    }

    @Override
    public void update(Line line) {
        jdbcTemplate.update("UPDATE LINE SET name = ?, color = ?, extra_fare = ? WHERE id = ?",
                line.getName(),
                line.getColor(),
                line.getExtraFare(),
                line.getId()
        );
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM LINE WHERE id = ?", id);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM LINE");
    }
}
