package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.service.PathService;
import javax.validation.Valid;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> createPath(@Valid final PathRequest pathRequest) {
        final PathResponse response = pathService.createPath(
                pathRequest.getSource(),
                pathRequest.getTarget(),
                pathRequest.getAge()
        );

        return ResponseEntity.ok().body(response);
    }
}
