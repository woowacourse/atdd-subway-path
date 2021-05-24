package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.service.PathService;
import wooteco.subway.station.domain.Station;

@Controller
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    // TODO: 경로조회 기능 구현하기
    @GetMapping
    public ResponseEntity<PathResponse> findPath(@RequestParam long source, @RequestParam long target) {
        Station sourceStation = pathService.findStationById(source);
        Station targetStation = pathService.findStationById(target);
        Path path = pathService.getPath(sourceStation, targetStation);

        return ResponseEntity.ok().body(PathResponse.of(path));
    }
}
