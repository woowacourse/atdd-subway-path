package wooteco.subway.admin.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;

import java.util.List;

public interface LineRepository extends CrudRepository<Line, Long> {
    @Override
    List<Line> findAll();

    @Query("select * from line_station")
    List<LineStation> findAllLineStations();

    @Query("select * from line_station where pre_station_id = :preStationId and station_id = :stationId limit 1")
    LineStation findLineStationByPreStationIdAndStationId(
            @Param("preStationId") Long preStationId,
            @Param("stationId") Long stationId);
}
