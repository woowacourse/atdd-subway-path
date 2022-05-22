package wooteco.subway.dao;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import wooteco.subway.dto.line.LineRequest;
import wooteco.subway.dto.line.LineResponse;

@Component
public class LineDao {

    private final RowMapper<LineResponse> lineRowMapper = (rs, rowNum) -> new LineResponse(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("color"),
            rs.getInt("extraFare")
    );

    private final JdbcTemplate jdbcTemplate;

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public LineResponse create(LineRequest line) {
        var sql = "INSERT INTO line (name, color, extraFare) VALUES(?, ?, ?)";
        var keyHolder = new GeneratedKeyHolder();
        create(line, sql, keyHolder);
        return new LineResponse(keyHolder.getKey().longValue(), line.getName(), line.getColor(), line.getExtraFare());
    }

    private void create(LineRequest line, String sql, KeyHolder keyHolder) {
        try {
            jdbcTemplate.update(connection -> {
                var statement = connection.prepareStatement(sql, new String[]{"id"});
                statement.setString(1, line.getName());
                statement.setString(2, line.getColor());
                statement.setInt(3, line.getExtraFare());
                return statement;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("[ERROR] 이미 존재하는 노선 정보 입니다.");
        }
    }

    public LineResponse find(Long id) {
        var sql = "SELECT * FROM line WHERE id=?";

        try {
            return jdbcTemplate.queryForObject(sql, lineRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("[ERROR] 해당 노선이 존재하지 않습니다.");
        }
    }

    public List<LineResponse> findAll() {
        var sql = "SELECT * FROM line";
        return jdbcTemplate.query(sql, lineRowMapper);
    }

    public void update(Long id, LineRequest lineRequest) {
        var sql = "UPDATE line SET name = ?, color = ?, extraFare = ? WHERE id = ?";
        var updatedRowCount = 0;

        try {
            var name = lineRequest.getName();
            var color = lineRequest.getColor();
            var extraFare = lineRequest.getExtraFare();
            updatedRowCount = jdbcTemplate.update(sql, name, color, extraFare, id);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("[ERROR] 이미 존재하는 노선 정보 입니다.");
        }

        checkUpdatedRow(updatedRowCount);
    }

    private void checkUpdatedRow(int updatedRowCount) {
        if (updatedRowCount == 0) {
            throw new NoSuchElementException("[ERROR] 해당 노선이 존재하지 않습니다.");
        }
    }

    public void deleteById(Long id) {
        var sql = "DELETE FROM line WHERE id=?";
        var deletedRowCount = jdbcTemplate.update(sql, id);

        checkUpdatedRow(deletedRowCount);
    }
}
