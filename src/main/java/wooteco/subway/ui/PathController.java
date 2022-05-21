package wooteco.subway.ui;

import org.springframework.web.bind.annotation.*;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(final PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public PathResponse findPath(@ModelAttribute final PathRequest pathRequest) {
        return pathService.getPath(pathRequest);
    }
}
