package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.dto.ShortestPath;
import wooteco.subway.admin.service.PathService;

import javax.websocket.server.PathParam;

@RestController
public class PathController {
	private PathService pathService;

	public PathController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping("/path/distance")
	public ResponseEntity<ShortestPath> findShortestDistancePath(@PathParam("sourceName") String sourceName,
																 @PathParam("targetName") String targetName,
																 @PathParam("criteria") String criteria) {

		ShortestPath shortestPath = pathService.findShortestDistancePath(sourceName, targetName, criteria);

		return ResponseEntity.ok()
				.body(shortestPath);
	}
}
