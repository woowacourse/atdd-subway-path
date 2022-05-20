package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.application.path.PathService;
import wooteco.subway.dto.PathResponse;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> searchPath(@RequestParam long source, @RequestParam long target) {
        PathResponse pathResponse = pathService.searchPath(source, target);
        return ResponseEntity.ok().body(pathResponse);
    }
}
