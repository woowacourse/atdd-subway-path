package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.service.PathService;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/path")
    public ResponseEntity<PathResponse> showPath(@RequestParam final Long source,
                                                 @RequestParam final Long target,
                                                 @RequestParam final int age) {
        PathResponse pathResponse = pathService.findShortestPath(source, target);
        return ResponseEntity.ok().body(pathResponse);
    }
}
