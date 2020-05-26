package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.PathService;

@Controller
public class PathController {
	private PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping("/path")
	public ResponseEntity<PathResponse> getPath(@RequestParam("source") String departStation,
		@RequestParam("target") String arrivalStation, @RequestParam("type") String type) {
		PathResponse paths = pathService.findPath(departStation, arrivalStation, type);
		return ResponseEntity.ok().body(paths);
	}
}
