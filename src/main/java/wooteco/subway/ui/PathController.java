package wooteco.subway.ui;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.application.path.PathService;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> searchPath(@Valid PathRequest pathRequest) {
        long source = pathRequest.getSource();
        long target = pathRequest.getTarget();
        int age = pathRequest.getAge();

        PathResponse pathResponse = pathService.searchPath(source, target, age);
        return ResponseEntity.ok().body(pathResponse);
    }
}
