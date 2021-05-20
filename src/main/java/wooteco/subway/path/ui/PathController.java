package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.application.dto.PathResponseDto;
import wooteco.subway.path.ui.dto.PathRequest;
import wooteco.subway.path.ui.dto.PathResponse;
import wooteco.subway.path.ui.dto.StationResponse;

import javax.validation.Valid;
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
    public ResponseEntity<PathResponse> showPath(@Valid @ModelAttribute PathRequest pathRequest) {
        PathResponseDto pathResponseDto = pathService.findPath(
                pathRequest.getSource(), pathRequest.getTarget()
        );
        List<StationResponse> stationsResponses = pathResponseDto.getStations().stream()
                .map(StationResponse::of)
                .collect(toList());

        return ResponseEntity.ok().body(new PathResponse(
                stationsResponses,
                pathResponseDto.getDistance()
        ));
    }

}
