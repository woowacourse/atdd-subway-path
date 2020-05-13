package wooteco.subway.admin.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import wooteco.subway.admin.domain.line.Line;
import wooteco.subway.admin.domain.line.LineStation;

public interface LineRepository extends CrudRepository<Line, Long> {
    @Query("select * from line_station")
    Set<LineStation> findAllLineStations();

    @Override
    List<Line> findAll();
}
