package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.ShortestPath;

import javax.websocket.server.PathParam;
import java.util.Arrays;
import java.util.List;

@RestController
public class PathController {
	@GetMapping("/path/distance")
	public ResponseEntity<ShortestPath> findShortestDistancePath(@PathParam("sourceName") String sourceName, @PathParam("targetName") String targetName) {
		List<Station> stations = Arrays.asList(
				new Station("시청"),
				new Station("충정로"),
				new Station("당산"),
				new Station("영등포구청"),
				new Station("신도림"));
		ShortestPath shortestPath = new ShortestPath(stations, 40, 40);

		return ResponseEntity.ok()
				.body(shortestPath);
	}
}
