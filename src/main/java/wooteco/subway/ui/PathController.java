package wooteco.subway.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.path.Path;
import wooteco.subway.service.PathService;
import wooteco.subway.service.dto.PathResponse;

@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public PathResponse getPath(@RequestParam Long source, @RequestParam Long target, @RequestParam Integer age) {
        Path path = pathService.getPath(source, target);
        Fare fare = pathService.getFare(source, target, age);
        return PathResponse.of(path, fare);
    }
}
