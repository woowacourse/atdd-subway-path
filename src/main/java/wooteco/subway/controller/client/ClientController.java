package wooteco.subway.controller.client;

import javax.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.WholeSubwayResponse;
import wooteco.subway.service.client.PathService;

@RestController
@Validated
public class ClientController {
	private PathService pathService;

	public ClientController(PathService pathService) {
		this.pathService = pathService;
	}

	@GetMapping("/lines/detail")
	public ResponseEntity<WholeSubwayResponse> retrieveLines() {
		WholeSubwayResponse wholeSubwayResponse = pathService.wholeLines();
		return ResponseEntity.ok()
			.eTag(String.valueOf(wholeSubwayResponse.hashCode()))
			.body(wholeSubwayResponse);
	}

	@GetMapping("/lines/path")
	public ResponseEntity<PathResponse> searchPath(@NotBlank String source,
		@NotBlank String target, @NotBlank String type) {
		PathResponse pathResponse = pathService.searchPath(source, target, type);
		return ResponseEntity.ok().body(pathResponse);
	}
}

