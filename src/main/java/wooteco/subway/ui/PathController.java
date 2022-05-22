package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.dto.path.PathResponse;
import wooteco.subway.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> showPath(@RequestParam(value = "source") final Long sourceStationId,
                                                 @RequestParam(value = "target") final Long targetStationId) {
        final PathResponse response = pathService.find(sourceStationId, targetStationId);
        return ResponseEntity.ok().body(response);
    }
}
