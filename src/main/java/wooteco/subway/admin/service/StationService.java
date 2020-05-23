package wooteco.subway.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.NotFoundResourceException;
import wooteco.subway.admin.repository.StationRepository;

@Transactional
@Service
public class StationService {
	private final StationRepository stationRepository;

	public StationService(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	@Transactional(readOnly = true)
	public List<StationResponse> findAll() {
		return StationResponse.listOf(stationRepository.findAll());
	}

	@Transactional(readOnly = true)
	public StationResponse findById(Long id) {
		Station station = stationRepository.findById(id)
			.orElseThrow(() -> new NotFoundResourceException("해당하는 역이 존재하지 않습니다."));
		return StationResponse.of(station);
	}

	public Station save(Station station) {
		return stationRepository.save(station);
	}

	public void deleteById(Long id) {
		if (!stationRepository.existsById(id)) {
			throw new NotFoundResourceException("해당하는 역이 존재하지 않습니다.");
		}
		stationRepository.deleteById(id);
	}
}
