package wooteco.subway.admin.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.SearchPathRequest;
import wooteco.subway.admin.service.PathService;

@RequestMapping("/paths")
@RestController
public class PathController {
    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getPath(@Valid SearchPathRequest searchPathRequest) {
        return ResponseEntity.ok(pathService.getPath(searchPathRequest));
    }
}
