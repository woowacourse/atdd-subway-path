package wooteco.subway.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.domain.path.implement.MinimumDistanceFindStrategy;
import wooteco.subway.domain.pricing.implement.DistanceProportionalPricingStrategy;
import wooteco.subway.service.PathService;
import wooteco.subway.service.dto.PathResponse;
import wooteco.subway.ui.dto.PathRequest;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public PathResponse searchPaths(PathRequest pathRequest) {
        return pathService.searchPaths(MinimumDistanceFindStrategy.of(), DistanceProportionalPricingStrategy.of(),
                pathRequest);
    }

}
