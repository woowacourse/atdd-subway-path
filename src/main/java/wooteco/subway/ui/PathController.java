package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.application.PathService;
import wooteco.subway.dto.PathResponse;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> searchPath(@RequestParam long source, @RequestParam long target, @RequestParam int age) {
        PathResponse response = pathService.searchPath(source, target, age);
        return ResponseEntity.ok().body(response);
    }
}
