package wooteco.subway.ui.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.ui.service.PathService;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> getPath(
            @RequestParam Long source, @RequestParam Long target, @RequestParam int age) {
        PathResponse pathResponse = pathService.getPath(source, target);
        return ResponseEntity.ok().body(pathResponse);
    }
}
