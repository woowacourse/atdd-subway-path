package wooteco.subway.admin.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import wooteco.subway.admin.domain.entity.Line;

public interface LineRepository extends CrudRep`ository<Line, Long> {
	@Override
	List<Line> findAll();
}
