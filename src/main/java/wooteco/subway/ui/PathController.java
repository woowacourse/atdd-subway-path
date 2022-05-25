package wooteco.subway.ui;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.application.PathService;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<?> searchPath(@Valid @ModelAttribute PathRequest request) {
        PathResponse response = pathService.searchPath(request.getSource(), request.getTarget(),
            request.getAge());
        return ResponseEntity.ok().body(response);
    }
}
