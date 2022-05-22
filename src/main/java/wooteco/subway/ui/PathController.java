package wooteco.subway.ui;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.service.PathService;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> showPath(@Valid final PathRequest pathRequest) {
        final PathResponse pathResponse = pathService.findShortestPath(pathRequest.getSource(),
                pathRequest.getTarget(), pathRequest.getAge());
        return ResponseEntity.ok().body(pathResponse);
    }
}
