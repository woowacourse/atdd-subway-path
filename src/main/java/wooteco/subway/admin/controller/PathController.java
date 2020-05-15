package wooteco.subway.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.LineService;

@Controller
public class PathController {
	private LineService lineService;

	public PathController(LineService lineService) {
		this.lineService = lineService;
	}

	@GetMapping("/path")
	public ResponseEntity<List<PathResponse>> getPath(@RequestParam("source") String departStation,
		@RequestParam("target") String arrivalStation) {
		List<PathResponse> paths = lineService.findPath(departStation, arrivalStation);
		return ResponseEntity.ok().body(paths);
	}
}
