package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.admin.dto.PathSearchResponse;
import wooteco.subway.admin.service.PathService;


@RequestMapping("/paths")
@RestController
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/{source}/{target}/type/{type}")
    public ResponseEntity<PathSearchResponse> searchPath(@PathVariable String source, @PathVariable String target, @PathVariable String type) {
        return ResponseEntity.ok().body(pathService.searchPath(source, target));
    }
}
