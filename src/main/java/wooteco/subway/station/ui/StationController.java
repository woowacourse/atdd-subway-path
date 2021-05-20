package wooteco.subway.station.ui;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.application.dto.StationResponseDto;
import wooteco.subway.station.ui.dto.StationRequest;
import wooteco.subway.station.ui.dto.StationResponse;
import wooteco.subway.station.ui.dto.valid.NumberValidation;

import javax.validation.Valid;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestMapping("/stations")
@RestController
public class StationController {

    private StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@Valid @RequestBody StationRequest stationRequest) {
        StationResponseDto stationResponseDto = stationService.saveStation(stationRequest);

        return ResponseEntity
                .created(URI.create("/stations/" + stationResponseDto.getId()))
                .body(new StationResponse(
                        stationResponseDto.getId(),
                        stationResponseDto.getName()
                ));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StationResponse>> showStations() {
        List<StationResponseDto> allStationResponses = stationService.findAllStationResponses();
        List<StationResponse> stationResponses = toStationResponses(allStationResponses);

        return ResponseEntity.ok().body(stationResponses);
    }

    private List<StationResponse> toStationResponses(List<StationResponseDto> allStationResponses) {
        return allStationResponses.stream()
                .map(StationResponse::of)
                .collect(toList());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@NumberValidation @PathVariable Long id) {
        stationService.deleteStationById(id);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }

}
