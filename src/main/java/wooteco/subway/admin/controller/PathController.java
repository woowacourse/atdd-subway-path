package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.service.PathService;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> calculatePath(@RequestParam Long source,
                                                      @RequestParam Long target,
                                                      @RequestParam PathType type) {
        return ResponseEntity.ok().body(pathService.calculatePath(new PathRequest(source, target, type)));
    }
}
