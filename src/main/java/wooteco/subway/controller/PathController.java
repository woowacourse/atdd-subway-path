package wooteco.subway.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.controller.dto.PathRequest;
import wooteco.subway.controller.dto.PathResponse;
import wooteco.subway.service.PathService;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@ModelAttribute @Valid PathRequest pathRequest) {
        return ResponseEntity.ok().body(PathResponse.from(
            pathService.findPath(pathRequest.getSource(), pathRequest.getTarget())));
    }
}
