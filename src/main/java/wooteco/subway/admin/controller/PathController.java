package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PathController {
    @GetMapping("/paths")
    public ResponseEntity<PathResponse> calculatePath(@RequestBody PathRequest view) {
        List<Station> stations = new ArrayList<>();
        stations.add(new Station(1L, "양재시민의숲역"));
        stations.add(new Station(2L, "양재역"));
        stations.add(new Station(3L, "강남역"));
        stations.add(new Station(4L, "역삼역"));
        stations.add(new Station(5L, "선릉역"));
        PathResponse pathResponse = new PathResponse(stations, 40, 40);
        return ResponseEntity.ok().body(pathResponse);
    }
}
