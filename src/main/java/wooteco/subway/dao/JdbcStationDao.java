package wooteco.subway.dao;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;

import wooteco.subway.domain.Station;

@Repository
public class JdbcStationDao implements StationDao {

    private final SimpleJdbcInsert jdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    public JdbcStationDao(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Station save(Station station) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(station);
        final Long id = jdbcInsert.executeAndReturnKey(param).longValue();
        return createNewObject(station, id);
    }

    private Station createNewObject(Station station, Long id) {
        Field field = ReflectionUtils.findField(Station.class, "id");
        field.setAccessible(true);
        ReflectionUtils.setField(field, station, id);
        return station;
    }

    @Override
    public List<Station> findAll() {
        final String sql = "SELECT * FROM station";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new Station(
                resultSet.getLong("id"),
                resultSet.getString("name")
        ));
    }

    @Override
    public Optional<Station> findById(Long id) {
        final String sql = "SELECT * FROM station WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> new Station(
                    resultSet.getLong("id"),
                    resultSet.getString("name")
            ), id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public int deleteById(Long id) {
        final String sql = "DELETE FROM station WHERE id = ?";
        final int deletedCount = jdbcTemplate.update(sql, id);
        validateRemoved(deletedCount, id);
        return deletedCount;
    }

    private void validateRemoved(int deletedCount, Long id) {
        if (deletedCount == 0) {
            throw new IllegalStateException(String.format("삭제하고자 하는 %d을(를) id로 가지는 역이 존재하지 않습니다.", id));
        }
    }
}
