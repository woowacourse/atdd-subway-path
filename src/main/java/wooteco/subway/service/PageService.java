package wooteco.subway.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.dto.StationResponse;
import wooteco.subway.repository.StationRepository;

@Service
public class PageService {
	private final StationRepository stationRepository;

	public PageService(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	public List<StationResponse> findAll() {
		return StationResponse.listOf(stationRepository.findAll());
	}
}
