package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;

@Controller
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(final Long source, final Long target) {
        return ResponseEntity.ok(pathService.findShortestPath(source, target));
    }
}
