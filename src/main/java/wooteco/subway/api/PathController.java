package wooteco.subway.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    ResponseEntity<PathResponse> findShortestPath(@ModelAttribute PathRequest pathRequest) {
        long source = pathRequest.getSource();
        long target = pathRequest.getTarget();
        PathResponse pathResponse = pathService.findShortestPath(source, target);
        return ResponseEntity.ok(pathResponse);
    }
}
