package wooteco.subway.admin.path.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.path.service.PathService;
import wooteco.subway.admin.path.service.dto.PathInfoResponse;
import wooteco.subway.admin.path.service.dto.PathRequest;

@RequestMapping("/paths")
@Validated
@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathInfoResponse> searchPath(@Valid PathRequest request) {
        PathInfoResponse pathInfoResponse = pathService.searchPath(request.getSource(), request.getTarget());

        return ResponseEntity
            .ok()
            .body(pathInfoResponse);
    }

}
