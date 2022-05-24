package wooteco.subway.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Section;
import wooteco.subway.entity.SectionEntity;
import wooteco.subway.exception.NotFoundException;

@Repository
public class JdbcSectionDao implements SectionDao {

    private static final String NOT_FOUND_STATION_ERROR_MESSAGE = "해당하는 역이 존재하지 않습니다.";
    private static final String NOT_FOUND_LINE_ERROR_MESSAGE = "해당하는 노선이 존재하지 않습니다.";

    private final JdbcTemplate jdbcTemplate;
    private final LineDao lineDao;
    private final StationDao stationDao;

    private RowMapper<SectionEntity> sectionRowMapper = (resultSet, rowNum) -> new SectionEntity(
            resultSet.getLong("id"),
            resultSet.getLong("line_id"),
            resultSet.getLong("up_station_id"),
            resultSet.getLong("down_station_id"),
            resultSet.getInt("distance")
    );

    public JdbcSectionDao(JdbcTemplate jdbcTemplate, LineDao lineDao, StationDao stationDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    @Override
    public Section save(Section section) {
        final String sql = "INSERT INTO SECTION (line_id, up_station_id, down_station_id, distance) VALUES (?, ?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, Long.toString(section.getLine().getId()));
            ps.setString(2, Long.toString(section.getUpStation().getId()));
            ps.setString(3, Long.toString(section.getDownStation().getId()));
            ps.setString(4, Long.toString(section.getDistance()));
            return ps;
        }, keyHolder);

        final Long newSectionId = keyHolder.getKey().longValue();

        return new Section(newSectionId, section.getLine(), section.getUpStation(),
                section.getDownStation(), section.getDistance());
    }

    @Override
    public void deleteById(Long id) {
        final String sql = "DELETE FROM SECTION WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Section findById(Long id) {
        try {
            final String sql = "SELECT * FROM SECTION WHERE id = ?";
            SectionEntity sectionEntity = jdbcTemplate.queryForObject(sql, sectionRowMapper, id);
            return new Section(sectionEntity.getId(),
                    lineDao.findById(sectionEntity.getLineId()).
                            orElseThrow(() -> new NotFoundException(NOT_FOUND_LINE_ERROR_MESSAGE)),
                    stationDao.findById(sectionEntity.getUpStationId()).
                            orElseThrow(() -> new NotFoundException(NOT_FOUND_STATION_ERROR_MESSAGE)),
                    stationDao.findById(sectionEntity.getDownStationId()).
                            orElseThrow(() -> new NotFoundException(NOT_FOUND_STATION_ERROR_MESSAGE)),
                    sectionEntity.getDistance());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Section> findByLineId(Long id) {
        try {
            final String sql = "SELECT * FROM SECTION WHERE line_id = ?";
            List<SectionEntity> sectionEntity = jdbcTemplate.query(sql, sectionRowMapper, id);
            List<Section> sections = new ArrayList<>();
            for (SectionEntity entity : sectionEntity) {
                sections.add(new Section(entity.getId(),
                        lineDao.findById(entity.getLineId()).
                                orElseThrow(() -> new NotFoundException(NOT_FOUND_LINE_ERROR_MESSAGE)),
                        stationDao.findById(entity.getUpStationId()).
                                orElseThrow(() -> new NotFoundException(NOT_FOUND_STATION_ERROR_MESSAGE)),
                        stationDao.findById(entity.getDownStationId()).
                                orElseThrow(() -> new NotFoundException(NOT_FOUND_STATION_ERROR_MESSAGE)),
                        entity.getDistance()));
            }

            return sections;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void update(Section section) {
        final String sql = "UPDATE SECTION SET line_id = ?, up_station_id = ?, down_station_id = ?, distance = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                section.getLine().getId(),
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                section.getDistance(),
                section.getId());
    }

    @Override
    public Section findByLineIdAndUpStationId(Long lineId, Long upStationId) {
        try {
            final String sql = "SELECT * FROM SECTION WHERE line_id = ? AND up_station_id = ?";
            SectionEntity sectionEntity = jdbcTemplate.queryForObject(sql, sectionRowMapper, lineId,
                    upStationId);
            return new Section(sectionEntity.getId(),
                    lineDao.findById(sectionEntity.getLineId()).
                            orElseThrow(() -> new NotFoundException(NOT_FOUND_LINE_ERROR_MESSAGE)),
                    stationDao.findById(sectionEntity.getUpStationId()).
                            orElseThrow(() -> new NotFoundException(NOT_FOUND_STATION_ERROR_MESSAGE)),
                    stationDao.findById(sectionEntity.getDownStationId()).
                            orElseThrow(() -> new NotFoundException(NOT_FOUND_STATION_ERROR_MESSAGE)),
                    sectionEntity.getDistance());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Section findByLineIdAndDownStationId(Long lineId, Long downStationId) {
        try {
            final String sql = "SELECT * FROM SECTION WHERE line_id = ? AND down_station_id = ?";
            SectionEntity sectionEntity = jdbcTemplate.queryForObject(sql, sectionRowMapper, lineId,
                    downStationId);
            return new Section(sectionEntity.getId(),
                    lineDao.findById(sectionEntity.getLineId()).
                            orElseThrow(() -> new NotFoundException(NOT_FOUND_LINE_ERROR_MESSAGE)),
                    stationDao.findById(sectionEntity.getUpStationId()).
                            orElseThrow(() -> new NotFoundException(NOT_FOUND_STATION_ERROR_MESSAGE)),
                    stationDao.findById(sectionEntity.getDownStationId()).
                            orElseThrow(() -> new NotFoundException(NOT_FOUND_STATION_ERROR_MESSAGE)),
                    sectionEntity.getDistance());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Section> findAll() {
        final String sql = "SELECT * FROM SECTION";
        List<SectionEntity> sectionEntities = jdbcTemplate.query(sql, sectionRowMapper);
        List<Section> sections = new LinkedList<>();
        for (SectionEntity sectionEntity : sectionEntities) {
            sections.add(new Section(sectionEntity.getId(),
                    lineDao.findById(sectionEntity.getLineId()).
                            orElseThrow(() -> new NotFoundException(NOT_FOUND_LINE_ERROR_MESSAGE)),
                    stationDao.findById(sectionEntity.getUpStationId()).
                            orElseThrow(() -> new NotFoundException(NOT_FOUND_STATION_ERROR_MESSAGE)),
                    stationDao.findById(sectionEntity.getDownStationId()).
                            orElseThrow(() -> new NotFoundException(NOT_FOUND_STATION_ERROR_MESSAGE)),
                    sectionEntity.getDistance()));
        }
        return sections;
    }
}
