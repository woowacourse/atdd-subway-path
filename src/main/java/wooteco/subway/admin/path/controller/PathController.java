package wooteco.subway.admin.path.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.path.service.PathService;
import wooteco.subway.admin.path.service.dto.PathInfoResponse;

@RequestMapping("/paths")
@RestController
public class PathController {

	private PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping
	public ResponseEntity<PathInfoResponse> searchPath(@RequestParam("source") Long source,
		@RequestParam("target") Long target) {
		PathInfoResponse pathInfoResponse = pathService.searchPath(source, target);

		return ResponseEntity
			.ok()
			.body(pathInfoResponse);
	}
}
