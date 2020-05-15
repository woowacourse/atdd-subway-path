package wooteco.subway.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.repository.StationRepository;

@Transactional
@Service
public class StationService {
	private final StationRepository stationRepository;

	public StationService(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	@Transactional(readOnly = true)
	public List<Station> findAll() {
		return stationRepository.findAll();
	}

	public Station save(Station station) {
		return stationRepository.save(station);
	}

	public void deleteById(Long id) {
		stationRepository.deleteById(id);
	}
}
