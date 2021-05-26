package wooteco.subway.path.ui;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.service.PathService;

@Controller
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PathResponse> getShortestDistancePath(@RequestParam Long source,
                                                                @RequestParam Long target) {

        return ResponseEntity.ok(pathService.getShortestDistancePath(source, target));
    }
}
