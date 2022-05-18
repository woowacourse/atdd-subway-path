package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.service.PathService;

@Controller
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> showPath(@RequestParam Long source, @RequestParam Long target,
        @RequestParam Integer age) {
        PathResponse pathResponse = pathService.findShortestPath(source, target);
        return ResponseEntity.ok().body(pathResponse);
    }
}
