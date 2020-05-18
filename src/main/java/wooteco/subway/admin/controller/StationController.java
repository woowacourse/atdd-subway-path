package wooteco.subway.admin.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.service.LineService;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final LineService lineService;

    public StationController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(
        @RequestBody StationCreateRequest stationCreateRequest) {
        Station persistStation = lineService.addStation(stationCreateRequest.toStation());

        return ResponseEntity
            .created(URI.create("/stations/" + persistStation.getId()))
            .body(StationResponse.of(persistStation));
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> showStations() {
        List<Station> stations = lineService.showStations();

        return ResponseEntity.ok().body(StationResponse.listOf(stations));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        lineService.deleteStationById(id);

        return ResponseEntity.noContent().build();
    }
}
