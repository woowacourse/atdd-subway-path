package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    // TODO: 경로조회 기능 구현하기
    @GetMapping
    public ResponseEntity<PathResponse> getPath(@RequestParam Long source, @RequestParam Long target) {
        PathResponse pathResponse = pathService.shortestPath(source, target);
        return ResponseEntity.ok(pathResponse);
    }
}
