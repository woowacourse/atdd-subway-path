package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.subway.path.PathService;
import wooteco.subway.path.dto.PathResponse;

@RequestMapping("/api/paths")
public class PathController {
    // TODO: 경로조회 기능 구현하기
      private final PathService service;

    public PathController(PathService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<PathResponse> showPath(@RequestParam("source") Long sourceId, @RequestParam("target") Long targetId) {
        return ResponseEntity.ok(service.findPath(sourceId, targetId));
    }
}
