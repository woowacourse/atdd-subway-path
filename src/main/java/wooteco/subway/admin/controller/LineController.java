package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.dto.request.LineRequest;
import wooteco.subway.admin.dto.request.LineStationCreateRequest;
import wooteco.subway.admin.dto.response.LineDetailResponse;
import wooteco.subway.admin.dto.response.LineResponse;
import wooteco.subway.admin.dto.response.WholeSubwayResponse;
import wooteco.subway.admin.service.LineService;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
	private final LineService lineService;

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
	public ResponseEntity<List<LineResponse>> showLines() {
		return ResponseEntity.ok()
				.body(LineResponse.listOf(lineService.showLines()));
	}

	@GetMapping("/lines/detail")
	public ResponseEntity<WholeSubwayResponse> wholeLines() {
		WholeSubwayResponse result = lineService.showLinesDetail();
		return ResponseEntity.ok().eTag(String.valueOf(result.hashCode())).body(result);
	}

	@GetMapping("/lines/{id}")
	public ResponseEntity<LineDetailResponse> retrieveLine(@PathVariable Long id) {
		return ResponseEntity.ok()
				.body(lineService.findLineWithStationsById(id));
	}

	@PutMapping("/lines/{id}")
	public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest view) {
		lineService.updateLine(id, view);
		return ResponseEntity.ok()
				.build();
	}

	@DeleteMapping("/lines/{id}")
	public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
		lineService.deleteLineById(id);
		return ResponseEntity.noContent()
				.build();
	}

	@PostMapping("/lines/{lineId}/stations")
	public ResponseEntity<Void> addLineStation(@PathVariable Long lineId, @RequestBody LineStationCreateRequest view) {
		lineService.addLineStation(lineId, view);
		return ResponseEntity.ok()
				.build();
	}

	@DeleteMapping("/lines/{lineId}/stations/{stationId}")
	public ResponseEntity<Void> removeLineStation(@PathVariable Long lineId, @PathVariable Long stationId) {
		lineService.removeLineStation(lineId, stationId);
		return ResponseEntity.noContent()
				.build();
	}
}
