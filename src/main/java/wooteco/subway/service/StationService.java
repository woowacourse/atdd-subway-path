package wooteco.subway.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.repository.StationRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.domain.Station;

@Service
@Transactional(readOnly = true)
public class StationService {

	private final StationRepository stationRepository;
	private final SectionRepository sectionRepository;

	public StationService(StationRepository stationRepository, SectionRepository sectionRepository) {
		this.stationRepository = stationRepository;
		this.sectionRepository = sectionRepository;
	}

	@Transactional
	public Station create(String name) {
		validateNameNotDuplicated(name);
		Long stationId = stationRepository.save(new Station(name));
		return stationRepository.findById(stationId);
	}

	private void validateNameNotDuplicated(String name) {
		if (stationRepository.existsByName(name)) {
			throw new IllegalArgumentException("해당 이름의 지하철 역이 이미 존재합니다.");
		}
	}

	public List<Station> findAllStations() {
		return stationRepository.findAll();
	}

	@Transactional
	public void remove(Long id) {
		if (sectionRepository.existByStation(id)) {
			throw new IllegalArgumentException("구간으로 등록되어 있어 삭제할 수 없습니다.");
		}
		stationRepository.remove(id);
	}

	public Station findOne(Long id) {
		return stationRepository.findById(id);
	}
}
