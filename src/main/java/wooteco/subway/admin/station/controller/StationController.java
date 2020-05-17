package wooteco.subway.admin.station.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.station.domain.Station;
import wooteco.subway.admin.station.repository.StationRepository;
import wooteco.subway.admin.station.service.dto.StationCreateRequest;
import wooteco.subway.admin.station.service.dto.StationResponse;

@RestController
public class StationController {
	private final StationRepository stationRepository;

	public StationController(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	@PostMapping("/stations")
	public ResponseEntity<StationResponse> createStation(@RequestBody StationCreateRequest view) {
		Station station = view.toStation();
		Station persistStation = stationRepository.save(station);

		return ResponseEntity
			.created(URI.create("/stations/" + persistStation.getId()))
			.body(StationResponse.of(persistStation));
	}

	@GetMapping("/stations")
	public ResponseEntity<List<StationResponse>> showStations() {
		return ResponseEntity.ok().body(StationResponse.listOf(stationRepository.findAll()));
	}

	@DeleteMapping("/stations/{id}")
	public ResponseEntity deleteStation(@PathVariable Long id) {
		stationRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
