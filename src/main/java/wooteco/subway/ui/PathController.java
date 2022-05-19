package wooteco.subway.ui;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.application.path.PathService;
import wooteco.subway.domain.path.PathSummary;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> searchPath(@RequestParam long source, @RequestParam long target) {
        PathSummary summary = pathService.searchPath(source, target);

        List<StationResponse> responses = summary.getPath().stream()
            .map(StationResponse::new)
            .collect(Collectors.toList());

        return ResponseEntity.ok().body(new PathResponse(responses, summary.getDistance(),
            summary.getFare()));
    }
}
