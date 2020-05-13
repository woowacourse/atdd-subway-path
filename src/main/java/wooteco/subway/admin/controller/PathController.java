package wooteco.subway.admin.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;

@RestController
@RequestMapping("search")
public class PathController {

	@GetMapping
	public ResponseEntity<List<PathResponse>> search(@RequestParam(value = "startId") Long startId,
		@RequestParam(value = "endId") Long endId) {
		List<PathResponse> pathResponses = new ArrayList<>();

		pathResponses.add(
			PathResponse.of(Arrays.asList(new Station("강남역"), new Station("역삼역"), new Station("삼성역")), 30, 30));
		pathResponses.add(
			PathResponse.of(Arrays.asList(new Station("강남역"), new Station("역삼역"), new Station("삼성역")), 30, 30));

		return ResponseEntity.ok().body(pathResponses);
	}
}
