package wooteco.subway.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

@Repository
public class LineDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public LineDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Line> findAll() {
        final String sql = "SELECT * FROM line";
        return jdbcTemplate.query(sql, this::lineRowMapper);
    }

    private Line lineRowMapper(ResultSet resultSet, int rowNum) throws SQLException {
        return new Line(resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("color"),
            resultSet.getInt("extraFare"),
            new Sections(findSectionsByLineId(resultSet.getLong("id"))));
    }

    private List<Section> findSectionsByLineId(long lineId) {
        final String sql = "SELECT section.id AS section_id,"
            + "       section.distance AS section_distance,\n"
            + "       us.id           AS up_station_id,\n"
            + "       us.name         AS up_station_name,\n"
            + "       ds.id           AS down_station_id,\n"
            + "       ds.name         AS down_station_name\n"
            + "FROM section section\n"
            + "         LEFT OUTER JOIN station us ON section.UP_STATION_ID = us.ID\n"
            + "         LEFT OUTER JOIN station ds ON section.DOWN_STATION_ID = ds.ID\n"
            + "WHERE section.line_id = :line_id";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("line_id", lineId);
        return jdbcTemplate.query(sql, parameterSource, toSection(lineId));
    }

    private RowMapper<Section> toSection(long lineId) {
        return (resultSet, rowNum) -> new Section(
            resultSet.getLong("section_id"),
            lineId,
            new Station(resultSet.getLong("up_station_id"), resultSet.getString("up_station_name")),
            new Station(resultSet.getLong("down_station_id"), resultSet.getString("down_station_name")),
            resultSet.getInt("section_distance")
        );
    }

    public Line findById(Long id) {
        final String sql = "SELECT * FROM line WHERE id = :line_id";

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("line_id", id);

        return jdbcTemplate.queryForObject(sql, paramSource, this::lineRowMapper);
    }

    public Line save(Line line) {
        final String sql = "INSERT INTO line(name, color, extraFare) VALUES(:name, :color, :extraFare)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(line);

        jdbcTemplate.update(sql, paramSource, keyHolder, new String[]{"ID"});
        return new Line(keyHolder.getKey().longValue(), line.getName(), line.getColor(),
            line.getExtraFare());
    }

    public void update(Line line) {
        final String sql = "UPDATE line SET name = :name, color = :color, extraFare = :extraFare "
            + "WHERE id = :id";
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(line);

        jdbcTemplate.update(sql, paramSource);
    }

    public void deleteById(Long id) {
        final String sql = "DELETE FROM line WHERE id = :id";
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);

        jdbcTemplate.update(sql, paramSource);
    }

    public boolean existByName(String name) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM line WHERE name = :name)";
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("name", name);

        return jdbcTemplate.queryForObject(sql, paramSource, Integer.class) != 0;
    }

    public boolean existById(Long id) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM line WHERE id = :id)";
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);

        return jdbcTemplate.queryForObject(sql, paramSource, Integer.class) != 0;
    }
}
