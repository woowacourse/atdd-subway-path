package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.application.PathService;
import wooteco.subway.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> getPath(@RequestParam(name = "source") Long sourceStationId,
                                                @RequestParam(name = "target") Long targetStationId) {
        final PathResponse pathResponse = pathService.getPath(sourceStationId, targetStationId);
        return ResponseEntity.ok().body(pathResponse);
    }

}
