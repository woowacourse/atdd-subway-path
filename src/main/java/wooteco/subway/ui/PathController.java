package wooteco.subway.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.DistanceProportionalStrategy;
import wooteco.subway.domain.MinimumDistanceStrategy;
import wooteco.subway.service.PathService;
import wooteco.subway.service.dto.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public PathResponse searchPaths(@RequestParam Long source, @RequestParam Long target, @RequestParam int age) {
        return pathService.searchPaths(new MinimumDistanceStrategy(), new DistanceProportionalStrategy(), source, target);
    }

}
