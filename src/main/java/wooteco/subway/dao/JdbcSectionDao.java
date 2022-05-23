package wooteco.subway.dao;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.dao.entity.SectionEntity;

@Repository
public class JdbcSectionDao implements SectionDao {

    private static final String TABLE_NAME = "SECTION";
    private static final String KEY_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcSectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_NAME);
    }

    @Override
    public SectionEntity save(SectionEntity sectionEntity) {
        Long id = insertActor.executeAndReturnKey(
                        Map.of("line_id", sectionEntity.getLineId(),
                                "up_station_id", sectionEntity.getUpStationId(),
                                "down_station_id", sectionEntity.getDownStationId(),
                                "distance", sectionEntity.getDistance()))
                .longValue();
        return findById(id);
    }

    @Override
    public int saveAll(List<SectionEntity> sectionEntities) {
        int[] inserted = insertActor.executeBatch(SqlParameterSourceUtils.createBatch(sectionEntities));
        return inserted.length;
    }

    @Override
    public List<SectionEntity> findAll() {
        String sql = "select * from SECTION";
        return jdbcTemplate.query(sql, mapper());
    }

    @Override
    public SectionEntity findById(Long id) {
        String sql = "select * from SECTION where id = :id";
        return jdbcTemplate.queryForObject(sql, Map.of("id", id), mapper());
    }

    @Override
    public List<SectionEntity> findByLineId(Long id) {
        String sql = "select * from SECTION where line_id = :id";
        return jdbcTemplate.query(sql, Map.of("id", id), mapper());
    }

    @Override
    public void update(SectionEntity sectionEntity) {
        String sql = "update SECTION set up_station_id = :upStationId, down_station_id = :downStationId where id = :id";
        jdbcTemplate.update(sql, Map.of("upStationId", sectionEntity.getUpStationId(),
                "downStationId", sectionEntity.getDownStationId(), "id", sectionEntity.getId()));
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from SECTION where id = :id";
        jdbcTemplate.update(sql, Map.of("id", id));
    }

    private RowMapper<SectionEntity> mapper() {
        return (resultSet, rowNum) ->
                new SectionEntity(
                        resultSet.getLong("id"),
                        resultSet.getLong("line_id"),
                        resultSet.getLong("up_station_id"),
                        resultSet.getLong("down_station_id"),
                        resultSet.getInt("distance")
                );
    }
}
