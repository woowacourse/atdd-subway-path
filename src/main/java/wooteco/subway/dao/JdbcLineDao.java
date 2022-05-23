package wooteco.subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;
import wooteco.subway.dto.LineRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcLineDao implements LineDao {
    private static final RowMapper<Line> LINE_ROW_MAPPER = (resultSet, rowNum) -> new Line(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("color"),
            resultSet.getInt("extraFare")
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcLineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("LINE")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(LineRequest lineRequest) {
        final SqlParameterSource sqlParameterSource =
                new MapSqlParameterSource("name", lineRequest.getName())
                        .addValue("color", lineRequest.getColor())
                        .addValue("extraFare", lineRequest.getExtraFare());

        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    @Override
    public Optional<Line> findById(Long id) {
        final String sql = "select * from LINE where id = :id";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Map.of("id", id), LINE_ROW_MAPPER));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Line> findByIds(List<Long> ids) {
        final String sql = "select * from LINE where id in (:ids)";
        return jdbcTemplate.query(sql, Map.of("ids", ids), LINE_ROW_MAPPER);
    }

    @Override
    public List<Line> findAll() {
        final String sql = "select * from LINE";
        return jdbcTemplate.query(sql, LINE_ROW_MAPPER);
    }

    @Override
    public boolean hasLine(String name) {
        final String sql = "select exists (select * from LINE where name = :name)";
        return jdbcTemplate.queryForObject(sql, Map.of("name", name), Boolean.class);
    }

    @Override
    public void updateById(Long id, String name, String color, int extraFare) {
        final String sql = "update LINE set name = :name, color = :color, extraFare = :extraFare where id = :id";

        final SqlParameterSource sqlParameterSource =
                new MapSqlParameterSource("name", name)
                        .addValue("color", color)
                        .addValue("extraFare", extraFare)
                        .addValue("id", id);

        jdbcTemplate.update(sql, sqlParameterSource);
    }

    @Override
    public void deleteById(Long id) {
        final String sql = "delete from LINE where id = :id";
        jdbcTemplate.update(sql, Map.of("id", id));
    }
}
