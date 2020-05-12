package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.service.PathService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

@RestController
public class PathController {

    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> retrieve(@RequestParam(name = "source", defaultValue = "") String source,
                                                 @RequestParam(name = "target", defaultValue = "") String target) throws UnsupportedEncodingException {

        String decodedSource = URLDecoder.decode(source, "UTF-8");
        String decodedTarget = URLDecoder.decode(target, "UTF-8");

        List<Station> stations = pathService.retrieve(decodedSource, decodedTarget);
        List<StationResponse> stationResponses = StationResponse.listOf(stations);

        PathResponse pathResponse = PathResponse.of(stationResponses, 40, 40);

        return ResponseEntity.ok(pathResponse);
    }
}
