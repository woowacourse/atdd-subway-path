package wooteco.subway.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.line.Line;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SectionDao sectionDao; // TODO: DAO가 DAO를 참조하는 구조 개선
    private final RowMapper<Line> lineRowMapper;

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sectionDao = new SectionDao(jdbcTemplate);

        // TODO: lineRowMapper 를 생성자에서 초기화하는 구조 개선
        this.lineRowMapper = (resultSet, rowNum) -> new Line(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("color"),
                new Fare(resultSet.getInt("extra_fare")),
                sectionDao.findSectionsByLineId(resultSet.getLong("id"))
        );
    }

    public Line save(Line line) {
        String sql = "INSERT INTO LINE (name, color, extra_fare) VALUES (?, ?, ?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, line.getName());
            ps.setString(2, line.getColor());
            ps.setInt(3, line.getExtraFare().getValue());
            return ps;
        }, keyHolder);

        long lineId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Line(lineId, line.getName(), line.getColor(), line.getExtraFare(), line.getSections());
    }

    // TODO: Entity 및 Repository 도입하고 개선해보기
    public Optional<Line> findById(Long id) {
        String sql = "SELECT id, name, color, extra_fare FROM LINE WHERE id = ?";
        List<Line> lines = jdbcTemplate.query(sql, lineRowMapper, id);
        return Optional.ofNullable(DataAccessUtils.singleResult(lines));
    }

    public Optional<Line> findByName(String name) {
        String sql = "SELECT id, name, color, extra_fare FROM LINE WHERE name = ?";
        List<Line> lines = jdbcTemplate.query(sql, lineRowMapper, name);
        return Optional.ofNullable(DataAccessUtils.singleResult(lines));
    }

    public List<Line> findAll() {
        String sql = "SELECT id, name, color, extra_fare FROM LINE";
        return jdbcTemplate.query(sql, lineRowMapper);
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
