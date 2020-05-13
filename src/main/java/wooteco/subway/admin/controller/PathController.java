package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.LineService;
import wooteco.subway.admin.service.PathService;

@RestController
public class PathController {
    private final LineService lineService;
    private final PathService pathService;

    public PathController(LineService lineService, PathService pathService) {
        this.lineService = lineService;
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@RequestParam String source,
                                                 @RequestParam String target,
                                                 @RequestParam String type) {
        return ResponseEntity.ok()
                .body(pathService.calculatePath(source, target, type));
    }
}
