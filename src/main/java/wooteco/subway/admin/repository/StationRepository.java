package wooteco.subway.admin.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wooteco.subway.admin.domain.Station;

import java.util.List;

public interface StationRepository extends CrudRepository<Station, Long> {
    @Override
    List<Station> findAllById(Iterable ids);

    @Override
    List<Station> findAll();

    @Query("SELECT name FROM STATION WHERE ID IN (:ids)")
    List<String> findNamesByIds(@Param("ids") List<Long> ids);

    @Query("SELECT * FROM STATION WHERE ID IN (:ids)")
    List<Station> findStationsByIds(@Param("ids") List<Long> ids);
}