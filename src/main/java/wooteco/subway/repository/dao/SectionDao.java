package wooteco.subway.repository.dao;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import wooteco.subway.repository.table.SectionTable;

@Repository
public class SectionDao {

	private static final String NO_SUCH_ID_ERROR = "해당 id에 맞는 지하철 구간이 없습니다.";

	private final SimpleJdbcInsert insertActor;
	private final NamedParameterJdbcTemplate jdbcTemplate;

	public SectionDao(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.insertActor = new SimpleJdbcInsert(dataSource)
			.withTableName("section")
			.usingGeneratedKeyColumns("id");
	}

	public Long save(SectionTable section) {
		return insertActor.executeAndReturnKey(new BeanPropertySqlParameterSource(section))
			.longValue();
	}

	public void saveAll(List<SectionTable> sections) {
		insertActor.executeBatch(SqlParameterSourceUtils.createBatch(sections));
	}

	public SectionTable findById(Long id) {
		String sql = "select * from section where id = :id";
		try {
			return jdbcTemplate.queryForObject(sql, Map.of("id", id), getSectionMapper());
		} catch (EmptyResultDataAccessException exception) {
			throw new NoSuchElementException(NO_SUCH_ID_ERROR);
		}
	}

	public List<SectionTable> findByLineId(Long lineId) {
		String sql = "select * from section where line_id = :lineId";
		return jdbcTemplate.query(sql, Map.of("lineId", lineId), getSectionMapper());
	}

	private RowMapper<SectionTable> getSectionMapper() {
		return ((rs, rowNum) -> new SectionTable(
			rs.getLong(1),
			rs.getLong(2),
			rs.getLong(3),
			rs.getLong(4),
			rs.getInt(5)
		));
	}

	public void update(SectionTable section) {
		jdbcTemplate.update(getUpdateSql(), new BeanPropertySqlParameterSource(section));
	}

	public void updateAll(List<SectionTable> sections) {
		jdbcTemplate.batchUpdate(getUpdateSql(), SqlParameterSourceUtils.createBatch(sections));
	}

	private String getUpdateSql() {
		return "update section set "
			+ "up_station_id = :upStationId, "
			+ "down_station_id = :downStationId, "
			+ "distance = :distance "
			+ "where id = :id";
	}

	public void remove(Long id) {
		if (jdbcTemplate.update(getDeleteSql(), Map.of("id", id)) == 0) {
			throw new NoSuchElementException(NO_SUCH_ID_ERROR);
		}
	}

	public void removeAll(List<Long> ids) {
		List<Map<String, Long>> batchArgs = ids.stream()
			.map(id -> Map.of("id", id))
			.collect(toList());
		jdbcTemplate.batchUpdate(getDeleteSql(),
			SqlParameterSourceUtils.createBatch(batchArgs)
		);
	}

	private String getDeleteSql() {
		return "delete from section where id = :id";
	}

	public Boolean existByStationId(Long stationId) {
		String sql = "select exists (select * from section "
			+ "where up_station_id = :stationId or down_station_id = :stationId)";
		return jdbcTemplate.queryForObject(sql, Map.of("stationId", stationId), Boolean.class);
	}
}
