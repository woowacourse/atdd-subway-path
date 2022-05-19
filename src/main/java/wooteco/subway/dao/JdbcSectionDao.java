package wooteco.subway.dao;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import wooteco.subway.domain.Section;
import wooteco.subway.dto.SectionEntity;

@Repository
public class JdbcSectionDao implements SectionDao {

    private final RowMapper<SectionEntity> sectionRowMapper = (resultSet, rowMapper) -> new SectionEntity(
        resultSet.getLong("id"),
        resultSet.getLong("line_id"),
        resultSet.getLong("up_station_id"),
        resultSet.getLong("down_station_id"),
        resultSet.getInt("distance")
    );

    private final JdbcTemplate jdbcTemplate;

    public JdbcSectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SectionEntity save(Long lineId, Section section) {
        String sql = "INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance) VALUES(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setLong(1, lineId);
            preparedStatement.setLong(2, section.getUpStationId());
            preparedStatement.setLong(3, section.getDownStationId());
            preparedStatement.setLong(4, section.getDistance());
            return preparedStatement;
        }, keyHolder);
        long id = keyHolder.getKey().longValue();

        return new SectionEntity(
                id, lineId, section.getUpStationId(), section.getDownStationId(), section.getDistance());
    }

    @Override
    public List<SectionEntity> findByLine(Long lineId) {
        String sql = "SELECT * FROM SECTION WHERE line_id = ?";
        return jdbcTemplate.query(sql, sectionRowMapper, lineId);
    }

    @Override
    public SectionEntity findById(long id) {
        String sql = "SELECT * FROM SECTION WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, sectionRowMapper, id);
    }

    @Override
    public List<SectionEntity> findAll() {
        String sql = "SELECT * FROM SECTION";
        return jdbcTemplate.query(sql, sectionRowMapper);
    }

    @Override
    public void update(Long lineId, Section section) {
        String sql = "UPDATE SECTION SET up_station_id = ?, down_station_id = ?, distance = ? WHERE id = ? AND line_id = ?";
        jdbcTemplate.update(sql, section.getUpStationId(), section.getDownStationId(), section.getDistance(),
            section.getId(), lineId);
    }

    @Override
    public void deleteAll(Long lineId) {
        String sql = "DELETE FROM SECTION WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    @Override
    public void delete(Long lineId, Section section) {
        String sql = "DELETE FROM SECTION WHERE line_id = ? AND id = ?";
        jdbcTemplate.update(sql, lineId, section.getId());
    }

    @Override
    public boolean existSectionUsingStation(Long stationId) {
        String sql = "SELECT EXISTS (SELECT * FROM SECTION WHERE up_station_id = ? OR down_station_id = ?) AS SUCCESS";
        return jdbcTemplate.queryForObject(sql, Boolean.class, stationId, stationId);
    }
}
