package wooteco.subway.admin.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.PathRequest;
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
	public ResponseEntity<PathResponse> search(@Valid PathRequest pathRequest) {
		PathResponse pathResponse = graphService.searchPath(pathRequest.getSource(), pathRequest.getTarget(),
			pathRequest.getType());
		return ResponseEntity.ok().body(pathResponse);
	}
}
