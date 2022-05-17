package wooteco.subway.repository;

import java.util.List;

import wooteco.subway.domain.Station;

public interface StationRepository {
	Long save(Station station);

	List<Station> findAll();

	void remove(Long id);

	Station findById(Long id);

	Boolean existsByName(String name);
}
