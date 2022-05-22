package wooteco.subway.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import wooteco.subway.controller.dto.PathRequest;
import wooteco.subway.controller.dto.PathResponse;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;
import wooteco.subway.service.PathService;
import wooteco.subway.service.StationService;

@Controller
public class PathController {

	private final StationService stationService;
	private final PathService pathService;

	public PathController(StationService stationService, PathService pathService) {
		this.stationService = stationService;
		this.pathService = pathService;
	}

	@GetMapping("/paths")
	public ResponseEntity<PathResponse> findPath(@ModelAttribute @Valid PathRequest pathRequest) {
		Station source = stationService.findOne(pathRequest.getSource());
		Station target = stationService.findOne(pathRequest.getTarget());
		Path path = pathService.findPath(source, target, pathRequest.getAge());
		return ResponseEntity.ok().body(PathResponse.from(path));
	}
}
