package wooteco.subway.admin.path.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.path.service.PathService;
import wooteco.subway.admin.path.service.dto.PathRequest;
import wooteco.subway.admin.path.service.dto.PathResponse;

@RequestMapping("/paths")
@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> searchPath(@Valid PathRequest request) {
        PathResponse response = pathService.searchPath(request);

        return ResponseEntity
            .ok()
            .body(response);
    }

}
