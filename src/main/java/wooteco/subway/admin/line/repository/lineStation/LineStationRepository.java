package wooteco.subway.admin.line.repository.lineStation;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import wooteco.subway.admin.line.domain.lineStation.LineStation;

public interface LineStationRepository extends CrudRepository<LineStation, Long> {

	@Override
	List<LineStation> findAll();
}
