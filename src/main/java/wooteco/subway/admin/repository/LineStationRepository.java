package wooteco.subway.admin.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wooteco.subway.admin.domain.LineStation;

import java.util.Optional;

public interface LineStationRepository extends CrudRepository<LineStation, Long> {
    @Query("select * from line_station where station_id = :stationId and pre_station_id = :preStationId")
    Optional<LineStation> findById(@Param("preStationId") Long preStationId, @Param("stationId") Long stationId);
}