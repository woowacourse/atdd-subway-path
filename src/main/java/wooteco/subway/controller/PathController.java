package wooteco.subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.dto.PathFindRequest;
import wooteco.subway.dto.PathFindResponse;
import wooteco.subway.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathFindResponse> findPath(@ModelAttribute PathFindRequest request) {
        PathFindResponse response = pathService.findPath(request.getSource(), request.getTarget(), request.getAge());
        return ResponseEntity.ok(response);
    }
}
