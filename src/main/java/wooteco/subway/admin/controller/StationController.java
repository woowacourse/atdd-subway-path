package wooteco.subway.admin.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.service.StationService;

@RequestMapping("/stations")
@RestController
public class StationController {
	private final StationService stationService;

	public StationController(StationService stationService) {
		this.stationService = stationService;
	}

	@PostMapping
	public ResponseEntity<StationResponse> createStation(@RequestBody StationCreateRequest request) {
		Station station = request.toStation();
		Station persistStation = stationService.save(station);

		return ResponseEntity
			.created(URI.create("/stations/" + persistStation.getId()))
			.body(StationResponse.of(persistStation));
	}

	@GetMapping
	public ResponseEntity<List<StationResponse>> showStations() {
		return ResponseEntity.ok().body(stationService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<StationResponse> showStation(@PathVariable Long id) {
		return ResponseEntity.ok().body(stationService.findById(id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
		stationService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
