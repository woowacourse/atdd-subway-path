package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.domain.DijkstraEdgeWeightType;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.service.PathService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PathController {
	private final PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping("/paths")
	public ResponseEntity<PathResponse> searchPath(
			@RequestParam("source") Long sourceStationId,
			@RequestParam("target") Long targetStationId,
			@RequestParam("type") String typeName) {
		DijkstraEdgeWeightType edgeWeightType = DijkstraEdgeWeightType.of(typeName);
		PathResponse pathResponse = pathService.searchPath(sourceStationId, targetStationId, edgeWeightType);

		return ResponseEntity.ok().body(pathResponse);
	}

	@GetMapping("/paths/stations")
	public ResponseEntity<List<StationResponse>> findAllStations() {
		List<StationResponse> stationResponses = pathService.findAllStations().stream()
				.map(StationResponse::of)
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(stationResponses);
	}
}
