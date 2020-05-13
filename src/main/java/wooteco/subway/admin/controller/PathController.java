package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

import java.util.Arrays;
import java.util.List;

@RestController
public class PathController {

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@RequestParam("source") Long sourceId, @RequestParam("target") Long targetId) {
        List<StationResponse> stations = StationResponse.listOf(Arrays.asList(new Station("왕십리"),
                new Station("서울숲"), new Station("압구정로데오"), new Station("강남구청")));
        return ResponseEntity.ok().body(new PathResponse(stations, 3, 9));
    }
}
