package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.exceptions.IllegalPathException;
import wooteco.subway.admin.dto.ExceptionResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.LineService;
import wooteco.subway.admin.service.exceptions.NotFoundException;

@RestController
@RequestMapping("search")
public class PathController {

	private LineService lineService;

	public PathController(LineService lineService) {
		this.lineService = lineService;
	}

	@GetMapping
	public ResponseEntity<PathResponse> search(
		@RequestParam(value = "source") String source,
		@RequestParam(value = "target") String target,
		@RequestParam(value = "type") PathType type) {

		PathResponse pathResponse = lineService.searchPath(source, target, type);
		return ResponseEntity.ok().body(pathResponse);
	}

	@ExceptionHandler(value = IllegalPathException.class)
	public ResponseEntity<ExceptionResponse> handleException(
		IllegalPathException e) {
		return ResponseEntity.badRequest().body(ExceptionResponse.of(e));
	}

	@ExceptionHandler(value = NotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleException(NotFoundException e) {
		return ResponseEntity.badRequest().body(ExceptionResponse.of(e));
	}
}
