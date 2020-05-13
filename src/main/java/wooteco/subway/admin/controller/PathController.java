package wooteco.subway.admin.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;

@RequestMapping("/api/paths")
@RestController
public class PathController {

    @GetMapping()
    public ResponseEntity<PathResponse> findShortestPath() {

        List<Station> stations = Arrays.asList(new Station("양재시민숲역"),
            new Station("양재역"),
            new Station("강남역"),
            new Station("역삼역"),
            new Station("선릉역")
        );
        int distance = 40;
        int duration = 40;

        return ResponseEntity
            .ok()
            .body(new PathResponse(stations, distance, duration));
    }
}
