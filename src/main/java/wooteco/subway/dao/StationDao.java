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
import wooteco.subway.domain.Station;
import wooteco.subway.dto.section.SectionRequest;

@Component
public class StationDao {

    private final RowMapper<Station> stationRowMapper = (rs, rowNum) -> new Station(
            rs.getLong("id"),
            rs.getString("name")
    );
    private final JdbcTemplate jdbcTemplate;

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Station save(String name) {
        var sql = "INSERT INTO station (name) VALUES(?)";
        var keyHolder = new GeneratedKeyHolder();
        save(name, sql, keyHolder);
        return new Station(keyHolder.getKey().longValue(), name);
    }

    private void save(String name, String sql, KeyHolder keyHolder) {
        try {
            jdbcTemplate.update(connection -> {
                var statement = connection.prepareStatement(sql, new String[]{"id"});
                statement.setString(1, name);
                return statement;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("[ERROR] 이미 존재하는 역 이름 입니다.");
        }
    }

    public List<Station> findAll() {
        String sql = "SELECT * FROM station";
        return jdbcTemplate.query(sql, stationRowMapper);
    }

    public void deleteById(Long id) {
        var sql = "DELETE FROM station WHERE id=?";
        var deletedRowCount = jdbcTemplate.update(sql, id);

        if (deletedRowCount == 0) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 역 입니다.");
        }
    }

    public Station findById(Long id) {
        var sql = "SELECT * FROM station WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, stationRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 역입니다.");
        }
    }

    public List<Station> findBySection(SectionRequest section) {
        var sql = "SELECT * FROM station WHERE id = ? OR id = ?";
        return jdbcTemplate.query(sql, stationRowMapper, section.getUpStationId(), section.getDownStationId());
    }
}
