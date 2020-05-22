package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.domain.type.WeightType;
import wooteco.subway.admin.dto.ErrorResponse;
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
	public ResponseEntity findShortestPath(@RequestParam("source") String sourceName,
		@RequestParam("target") String targetName, @RequestParam("type") WeightType type) {
		PathResponse pathResponse = pathService.findPath(sourceName, targetName, type);
		return ResponseEntity.ok(pathResponse);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception e) {
		return ResponseEntity.badRequest().body(ErrorResponse.of(e));
	}
}
