package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.PathService;

@RestController
public class PathController {
	private final PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping("/path")
	public ResponseEntity<PathResponse> searchPath(
			@RequestParam("source") String source,
			@RequestParam("target") String target) {
		PathResponse pathResponse = pathService.searchPath(source, target, true);

		return ResponseEntity.ok().body(pathResponse);
	}
}
