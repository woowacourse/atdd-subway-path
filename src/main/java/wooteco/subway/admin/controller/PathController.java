package wooteco.subway.admin.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.ShortestPathResponse;
import wooteco.subway.admin.dto.StationResponse;

@RestController
@RequestMapping("/paths")
public class PathController {
	@GetMapping
	ResponseEntity<ShortestPathResponse> getShortestPath (@RequestParam String source, @RequestParam String target) {
		return ResponseEntity.ok(createMockShortestPaths());
	}

	private ShortestPathResponse createMockShortestPaths() {
		List<StationResponse> stations = Arrays.asList(
			new StationResponse(1L, "강남역", null),
			new StationResponse(2L, "역삼역", null),
			new StationResponse(3L, "선릉역", null),
			new StationResponse(4L, "사당역", null)
		);
		return new ShortestPathResponse(stations, 13, 10);
	}
}
