package wooteco.subway.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import wooteco.subway.domain.Line;

public interface LineRepository extends CrudRepository<Line, Long> {
    @Override
    List<Line> findAll();
}
