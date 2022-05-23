package wooteco.subway.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

@Repository
public class LineDao {

    private static final String NON_EXISTENT_ID_EXCEPTION = "존재하지 않는 id입니다.";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public LineDao(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public Line save(Line line) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", line.getName());
        parameters.put("color", line.getColor());
        parameters.put("extra_fare", line.getExtraFare());

        final Number number = simpleJdbcInsert.executeAndReturnKey(parameters);
        return Line.createWithoutSection(number.longValue(), line.getName(), line.getColor(), line.getExtraFare());
    }

    public boolean existsByName(String name) {
        final String sql = "SELECT COUNT(*) FROM line WHERE name = :name";
        final SqlParameterSource namedParameter = new MapSqlParameterSource("name", name);
        final Integer numOfLine = namedParameterJdbcTemplate.queryForObject(sql, namedParameter, Integer.class);
        return !numOfLine.equals(0);
    }

    public List<Line> findAll() {
        final String sql = "SELECT * FROM line";
        return namedParameterJdbcTemplate.query(sql, this::lineMapper);
    }

    public Line findById(Long id) {
        final String sql = "SELECT * FROM line WHERE id = :id";
        final SqlParameterSource namedParameter = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, namedParameter, this::lineMapper);
    }

    private Line lineMapper(ResultSet rs, int rowNum) throws SQLException {
        return Line.createWithId(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("color"),
                rs.getInt("extra_fare"),
                findSectionsByLineId(rs.getLong("id"))
        );
    }

    private List<Section> findSectionsByLineId(Long lineId) {
        final String sql = "select s.id sid, s.distance sdistance, us.id usid, us.name usname, ds.id dsid, ds.name dsname " +
                "from sections s " +
                "join station us on s.up_station_id = us.id " +
                "join station ds on s.down_station_id = ds.id " +
                "where line_id = :line_id";
        final SqlParameterSource namedParameter = new MapSqlParameterSource("line_id", lineId);
        return namedParameterJdbcTemplate.query(sql, namedParameter, ((rs, rowNum) -> {
            return Section.createWithId(rs.getLong("sid"), new Station(rs.getLong("usid"), rs.getString("usname")),
                    new Station(rs.getLong("dsid"), rs.getString("dsname")), rs.getInt("sdistance"));
        }));
    }

    public void updateLineById(Long id, String name, String color, int extraFare) {
        final String sql = "UPDATE line SET name=:name, color=:color, extra_fare=:extra_fare WHERE id=:id";
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("name", name);
        params.put("color", color);
        params.put("extra_fare", extraFare);
        final SqlParameterSource namedParameters = new MapSqlParameterSource(params);
        validateResult(namedParameterJdbcTemplate.update(sql, namedParameters));
    }

    public void deleteById(Long id) {
        final String sql = "DELETE FROM line WHERE id = :id";
        final SqlParameterSource namedParameter = new MapSqlParameterSource("id", id);
        validateResult(namedParameterJdbcTemplate.update(sql, namedParameter));
    }

    private void validateResult(int result) {
        if (result == 0) {
            throw new IllegalArgumentException(NON_EXISTENT_ID_EXCEPTION);
        }
    }

    public int findMaxExtraFareByLineIds(List<Long> usedLines) {
        final SqlParameterSource parameters = new MapSqlParameterSource("ids", usedLines);
        final String sql = "select max(extra_fare) from line where id in (:ids)";
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, Integer.class);
    }
}
