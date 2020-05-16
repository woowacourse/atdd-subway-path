package wooteco.subway.admin.repository;

import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wooteco.subway.admin.domain.Line;

public interface LineRepository extends CrudRepository<Line, Long> {

	@Override
	List<Line> findAll();

	@Query("SELECT *"
		+ "  FROM LINE"
		+ " WHERE LINE_ID IN (SELECT DISTINCT(ID)"
		+ "                     FROM LINE"
		+ "                     JOIN LINE_STATION ON LINE.ID = LINE_STATION.LINE"
		+ "                    WHERE LINE_STATION.STATION = :stationId)")
	List<Line> findLinesByStationId(@Param("stationId") Long stationId);
}
