package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.service.PathFindService;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final PathFindService pathFindService;

    public PathController(final PathFindService pathFindService) {
        this.pathFindService = pathFindService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findShortestPath(@RequestParam Long source, @RequestParam Long target){
        PathResponse pathResponse = pathFindService.findShortestPath(source, target);
        return ResponseEntity.ok().body(pathResponse);
    }
}
