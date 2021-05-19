package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;

@Controller
public class PathController {

    // [기능 추가]: 경로조회 기능 구현하기
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> path(@RequestParam Long source, @RequestParam Long target) {
        PathResponse pathResponse = pathService.findPath(source, target);
        return ResponseEntity.ok(pathResponse);
    }
}
