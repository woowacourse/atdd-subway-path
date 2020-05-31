package wooteco.subway.admin.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import wooteco.subway.admin.domain.EdgeWeightType;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.LineService;

@Controller
public class PathController {
	private LineService lineService;

	public PathController(LineService lineService) {
		this.lineService = lineService;
	}

	@GetMapping("/path")
	public ResponseEntity<List<PathResponse>> getPath(@RequestParam("source") String departStationName,
		@RequestParam("target") String arrivalStationName) {
		Station departStation = lineService.getStationByName(departStationName);
		Station arrivalStation = lineService.getStationByName(arrivalStationName);
		Stations stations = lineService.findAllStations();
		Lines lines = lineService.findAllLines();

		PathResponse distanceWeightPath = lineService.findPath(departStation, arrivalStation, stations, lines,
			EdgeWeightType.DISTANCE);
		PathResponse durationWeightPath = lineService.findPath(departStation, arrivalStation, stations, lines,
			EdgeWeightType.DURATION);
		List<PathResponse> paths = Arrays.asList(distanceWeightPath, durationWeightPath);

		return ResponseEntity.ok().body(paths);
	}
}
