package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.subway.exception.PathException;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;

@Controller
@RequestMapping("/api")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    // TODO: 경로조회 기능 구현하기
    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@RequestParam("source") Long sourceId, @RequestParam("target") Long targetId) {
        return ResponseEntity.ok(pathService.findPath(sourceId, targetId));
    }

    @ExceptionHandler(PathException.class)
    public ResponseEntity<String> PathException(PathException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
