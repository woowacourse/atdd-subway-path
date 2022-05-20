package wooteco.subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import wooteco.subway.controller.dto.PathRequest;
import wooteco.subway.controller.dto.PathResponse;
import wooteco.subway.domain.Path;
import wooteco.subway.service.PathService;

@Controller
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(PathRequest pathRequest) {
        Path path = pathService.findPath(pathRequest.getSource(), pathRequest.getTarget());
        return ResponseEntity.ok().body(PathResponse.from(path));
    }
}
