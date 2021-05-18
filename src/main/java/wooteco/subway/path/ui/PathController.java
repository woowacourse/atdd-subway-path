package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.application.dto.PathResponseDto;
import wooteco.subway.path.ui.dto.PathResponse;
import wooteco.subway.path.ui.dto.StationResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestMapping("/paths")
@RestController
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> showPath(@RequestParam Long source,
                                                 @RequestParam Long target) {
        PathResponseDto pathResponseDto = pathService.findPath(source, target);
        List<StationResponse> stationsResponses = pathResponseDto.getStations().stream()
                .map(StationResponse::of)
                .collect(toList());


        return ResponseEntity.ok().body(new PathResponse(
                stationsResponses,
                pathResponseDto.getDistance()
        ));
    }

}
