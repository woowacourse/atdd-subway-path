package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/api/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    // 경로조회 기능 구현하기
    @GetMapping
    public ResponseEntity<PathResponse> findShortestPaths(@RequestParam("source") Long sourceStationId,
                                                          @RequestParam("target") Long targetStationId) {
        return ResponseEntity.ok(pathService.findShortestPaths(sourceStationId, targetStationId));
    }
}
