package wooteco.subway.admin.line.controller.line;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.line.service.LineService;
import wooteco.subway.admin.line.service.dto.line.LineDetailResponse;
import wooteco.subway.admin.line.service.dto.line.LineRequest;
import wooteco.subway.admin.line.service.dto.line.LineResponse;
import wooteco.subway.admin.path.service.dto.WholeSubwayResponse;

@RequestMapping("/lines")
@RestController
public class LineController {

	private final LineService lineService;

	public LineController(LineService lineService) {
		this.lineService = lineService;
	}

	@GetMapping
	public ResponseEntity<List<LineResponse>> get() {
		return ResponseEntity
			.ok()
			.body(LineResponse.listOf(lineService.findAll()));
	}

	@PostMapping
	public ResponseEntity<LineResponse> save(@RequestBody @Valid LineRequest request) {
		final LineResponse response = lineService.save(request.toLine());

		return ResponseEntity
			.created(URI.create("/lines/" + response.getId()))
			.body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<LineDetailResponse> retrieveLine(@PathVariable Long id) {
		return ResponseEntity
			.ok()
			.body(lineService.findLineWithStationsById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> modify(@PathVariable Long id, @RequestBody @Valid LineRequest request) {
		lineService.updateLine(id, request);

		return ResponseEntity
			.ok()
			.build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remove(@PathVariable Long id) {
		lineService.deleteLineById(id);

		return ResponseEntity
			.noContent()
			.build();
	}

	@GetMapping("/detail")
	public ResponseEntity<WholeSubwayResponse> wholeLines() {
		return ResponseEntity
			.ok()
			.body(lineService.wholeLines());
	}

}
