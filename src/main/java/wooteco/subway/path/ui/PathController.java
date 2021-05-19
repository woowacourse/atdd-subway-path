package wooteco.subway.path.ui;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.path.PathService;
import wooteco.subway.path.dto.PathRequest;
import wooteco.subway.path.dto.PathResponse;

@RequestMapping("/api/paths")
@RestController
public class PathController {

    private final PathService service;

    public PathController(PathService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<PathResponse> showPath(@ModelAttribute @Valid PathRequest request) {
        return ResponseEntity.ok(service.findPath(request.getSource(), request.getTarget()));
    }
}
