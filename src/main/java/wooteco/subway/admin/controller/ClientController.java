package wooteco.subway.admin.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.service.LineService;

@RestController
public class ClientController {
    private LineService lineService;

    public ClientController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping("/lines/detail")
    public ResponseEntity<WholeSubwayResponse> retrieveLines() {
        WholeSubwayResponse wholeSubwayResponse = lineService.wholeLines();
        return ResponseEntity.ok()
            .eTag(String.valueOf(wholeSubwayResponse.hashCode()))
            .body(wholeSubwayResponse);
    }

    @GetMapping("/path")
    public ResponseEntity<PathResponse> searchPathByShortestDistance() {
        List<StationResponse> stations = Arrays.asList(new StationResponse(),
            new StationResponse(), new StationResponse());
        PathResponse pathResponse = new PathResponse(stations, 10, 10);

        return ResponseEntity.ok().body(pathResponse);
    }

}
