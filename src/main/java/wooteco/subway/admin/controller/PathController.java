package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.GraphService;

@RestController
@RequestMapping("search")
public class PathController {
	private final GraphService graphService;

	public PathController(final GraphService graphService) {
		this.graphService = graphService;
	}

	@GetMapping
	public ResponseEntity<PathResponse> search(@RequestParam(value = "source") String source,
		@RequestParam(value = "target") String target, @RequestParam(value = "type") PathType type) {

		PathResponse pathResponse = graphService.searchPath(source, target, type);
		return ResponseEntity.ok().body(pathResponse);
	}
}
