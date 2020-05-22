package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.domain.SearchType;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.PathService;

@RestController
public class PathController {
	private final PathService pathService;

	public PathController(final PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping("/paths")
	public ResponseEntity<PathResponse> searchPath(PathRequest pathRequest) {

		PathResponse pathResponse = pathService.searchPath(pathRequest.getSource(), pathRequest.getTarget(), pathRequest.getSearchType());

		return ResponseEntity.ok().body(pathResponse);
	}
}
