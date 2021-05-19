package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.path.apllication.PathService;
import wooteco.subway.path.dto.PathRequest;
import wooteco.subway.path.dto.PathResponse;

import javax.validation.Valid;

@RestController
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping(value = "/paths")
    public ResponseEntity<PathResponse> getSubwayPath(@RequestBody @Valid PathRequest pathRequest) {
        PathResponse response = pathService.findShortestPath(pathRequest.getSourceId(), pathRequest.getTargetId());
        return ResponseEntity.ok().body(response);
    }
}
