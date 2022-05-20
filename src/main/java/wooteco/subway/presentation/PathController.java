package wooteco.subway.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.application.PathService;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;

import javax.validation.Valid;

@RequestMapping("/paths")
@RestController
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@Valid @ModelAttribute PathRequest pathRequest) {
        PathResponse response = pathService.findPath(pathRequest.getSource(), pathRequest.getTarget(), pathRequest.getAge());
        return ResponseEntity.ok(response);
    }
}
