package wooteco.subway.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.service.PathService;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public PathResponse getPath(@RequestParam Long source, @RequestParam Long target, @RequestParam Integer age)
            throws Exception {
        return pathService.getPath(source, target, age);
    }
}
