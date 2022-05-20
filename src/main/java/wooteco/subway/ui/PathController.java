package wooteco.subway.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public PathResponse findPath(@RequestParam(defaultValue = "0") final long source, @RequestParam(defaultValue = "0") final long target, @RequestParam(defaultValue = "20") final int age) {
        return pathService.getPath(source, target, age);
    }
}
