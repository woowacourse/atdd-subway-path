package wooteco.subway.path.ui;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(
        @RequestParam(name = "source") @Valid @NotNull Long sourceStationId,
        @RequestParam("target") @Valid @NotNull Long targetStationId) {
        PathResponse pathResponse = pathService.findStationInPath(sourceStationId, targetStationId);

        return ResponseEntity.ok(pathResponse);
    }

}
