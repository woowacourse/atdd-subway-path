package wooteco.subway.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.application.PathService;
import wooteco.subway.dto.response.PathResponse;

@RequestMapping("/paths")
@RequiredArgsConstructor
@RestController
public class PathController {

    private final PathService pathService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PathResponse findPath(@RequestParam("source") Long fromStationId,
                                 @RequestParam("target") Long toStationId,
                                 @RequestParam(defaultValue = "0") Integer age) {
        return pathService.findPath(fromStationId, toStationId, age);
    }
}
