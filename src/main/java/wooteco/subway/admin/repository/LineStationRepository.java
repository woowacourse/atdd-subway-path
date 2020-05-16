package wooteco.subway.admin.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import wooteco.subway.admin.domain.LineStation;

public interface LineStationRepository extends CrudRepository<LineStation, Long> {

	@Override
	List<LineStation> findAll();
}
