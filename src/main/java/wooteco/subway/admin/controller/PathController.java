package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.service.LineService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PathController {
    private final LineService lineService;

    public PathController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@RequestParam String source,
                                                 @RequestParam String target,
                                                 @RequestParam String type) {
        List<StationResponse> stations = new ArrayList<>();
        stations.add(new StationResponse(1L, "강남역", null));
        stations.add(new StationResponse(1L, "역삼역", null));
        stations.add(new StationResponse(1L, "선릉역", null));
        return ResponseEntity.ok()
                .body(new PathResponse(stations, 20L, 20L));
    }
}
