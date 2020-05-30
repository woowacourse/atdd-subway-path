package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {
	private PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping
	public ResponseEntity<PathResponse> findShortestPath(@RequestBody PathRequest pathRequest) {
		PathResponse pathResponse = pathService.findPath(pathRequest.getSourceId(), pathRequest.getTargetId(),
			pathRequest.getWeightType());
		return ResponseEntity.ok(pathResponse);
	}
}
