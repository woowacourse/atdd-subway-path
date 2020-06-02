package wooteco.subway.admin.line.controller.lineStation;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.line.service.LineService;
import wooteco.subway.admin.line.service.dto.lineStation.LineStationCreateRequest;

@RequestMapping("/lines")
@RestController
public class LineStationController {

	private final LineService lineService;

	public LineStationController(LineService lineService) {
		this.lineService = lineService;
	}

	@PostMapping("/{lineId}/stations")
	public ResponseEntity<Void> addLineStation(@PathVariable Long lineId,
		@RequestBody @Valid LineStationCreateRequest request) {
		lineService.addLineStation(lineId, request);

		return ResponseEntity
			.ok()
			.build();
	}

	@DeleteMapping("/{lineId}/stations/{stationId}")
	public ResponseEntity<Void> deleteLineStation(@PathVariable Long lineId,
		@PathVariable Long stationId) {
		lineService.delete(lineId, stationId);

		return ResponseEntity
			.noContent()
			.build();
	}

}
