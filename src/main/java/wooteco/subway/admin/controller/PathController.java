package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

@RestController
public class PathController {

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> retrieve(@RequestParam(name = "source", defaultValue = "") String source,
                                                 @RequestParam(name = "target", defaultValue = "") String target) throws UnsupportedEncodingException {

        String decodedSource = URLDecoder.decode(source, "UTF-8");
        String decodedTarget = URLDecoder.decode(target, "UTF-8");

        List<Station> stations = createMock();
        List<StationResponse> stationResponses = StationResponse.listOf(stations);

        PathResponse pathResponse = PathResponse.of(stationResponses, 40, 40);

        return ResponseEntity.ok(pathResponse);
    }

    private List<Station> createMock() {
        return Arrays.asList(new Station("환-강남역"),
                new Station("1-역삼역"),
                new Station("환-삼성역"),
                new Station("1-삼송역"),
                new Station("환-지축역"));
    }
}
