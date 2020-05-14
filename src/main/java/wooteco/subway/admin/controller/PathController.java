package wooteco.subway.admin.controller;

import java.util.Arrays;

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
	public ResponseEntity<PathResponse> search(@RequestParam(value = "source") String source,
		@RequestParam(value = "target") String target, @RequestParam(value = "type") String type) {
		PathResponse pathResponses = PathResponse.of(
			Arrays.asList(new Station("강남역"), new Station("역삼역"), new Station("삼성역"))
			, 30, 30);
		return ResponseEntity.ok().body(pathResponses);
	}
}
