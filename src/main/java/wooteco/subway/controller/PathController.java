package wooteco.subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.controller.dto.path.PathRequest;
import wooteco.subway.service.PathService;
import wooteco.subway.service.dto.path.PathResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@Valid PathRequest pathRequest) {
        PathResponse pathResponse = pathService.getPath(pathRequest.toServiceRequest());

        return ResponseEntity.ok().body(pathResponse);
    }
}
