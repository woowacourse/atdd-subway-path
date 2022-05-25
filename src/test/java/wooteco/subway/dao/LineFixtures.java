package wooteco.subway.dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import wooteco.subway.domain.Line;

public class LineFixtures {

    public static void setUp(NamedParameterJdbcTemplate jdbcTemplate, Line... lines) {
        List<MapSqlParameterSource> params = new ArrayList<>();

        for (Line line : lines) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("name", line.getName());
            source.addValue("color", line.getColor());
            source.addValue("extraFare", line.getExtraFare());
            params.add(source);
        }

        jdbcTemplate.batchUpdate("INSERT INTO line (name, color, extraFare) VALUES (:name, :color, :extraFare)",
            params.toArray(MapSqlParameterSource[]::new));
    }
}
