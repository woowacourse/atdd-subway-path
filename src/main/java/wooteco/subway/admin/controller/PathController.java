package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.PathService;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity findPath(@RequestParam("source") String source, @RequestParam("target") String target, @RequestParam("type") String type) {
        PathResponse path;
        try {
            PathType pathType = PathType.of(type);
            path = pathService.findPath(source, target, pathType);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e);
        }
        return ResponseEntity.ok().body(path);
    }
}
