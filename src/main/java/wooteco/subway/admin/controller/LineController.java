package wooteco.subway.admin.controller;

import java.net.URI;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.entity.Line;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.service.LineService;

@RestController
public class LineController {
	private LineService lineService;

	public LineController(LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping(value = "/lines")
	public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest view) {
		Line persistLine = lineService.save(view.toLine());

		return ResponseEntity
			.created(URI.create("/lines/" + persistLine.getId()))
			.body(LineResponse.of(persistLine));
	}

	@GetMapping("/lines")
	public ResponseEntity<List<LineResponse>> showLine() {
		return ResponseEntity.ok().body(LineResponse.listOf(lineService.showLines()));
	}

	@GetMapping("/lines/{id}")
	public ResponseEntity<LineDetailResponse> retrieveLine(@PathVariable Long id) {
		return ResponseEntity.ok().body(lineService.findLineWithStationsById(id));
	}

	@CacheEvict(value = "lineDetail")
	@PutMapping("/lines/{id}")
	public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest view) {
		lineService.updateLine(id, view);
		return ResponseEntity.ok().build();
	}

	@CacheEvict(value = "lineDetail")
	@DeleteMapping("/lines/{id}")
	public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
		lineService.deleteLineById(id);
		return ResponseEntity.noContent().build();
	}

	@CacheEvict(value = "lineDetail")
	@PostMapping("/lines/{lineId}/stations")
	public ResponseEntity<Void> addLineStation(@PathVariable Long lineId, @RequestBody LineStationCreateRequest view) {
		lineService.addLineStation(lineId, view);
		return ResponseEntity.ok().build();
	}

	@CacheEvict(value = "lineDetail")
	@DeleteMapping("/lines/{lineId}/stations/{stationId}")
	public ResponseEntity<Void> removeLineStation(@PathVariable Long lineId, @PathVariable Long stationId) {
		lineService.removeLineStation(lineId, stationId);
		return ResponseEntity.noContent().build();
	}

	@Cacheable(value = "lineDetail")
	@GetMapping("/lines/detail")
	public ResponseEntity<WholeSubwayResponse> wholeLines() {
		WholeSubwayResponse body = lineService.wholeLines();
		return ResponseEntity.ok()
			.body(body);
	}
}
