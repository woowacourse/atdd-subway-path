package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.service.PathService;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> showPaths(@RequestParam Long source, @RequestParam Long target,
                                                  @RequestParam int age) {
        return ResponseEntity.ok(pathService.createPath(source, target, age));
    }
}
