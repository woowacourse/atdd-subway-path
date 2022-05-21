package wooteco.subway.ui;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.service.PathService;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping(value = "/paths", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PathResponse> findPath(@ModelAttribute PathRequest pathRequest) {
        return ResponseEntity.ok().body(pathService.findPath(pathRequest));
    }
}
