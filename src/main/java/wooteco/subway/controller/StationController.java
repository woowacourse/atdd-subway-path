package wooteco.subway.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.controller.dto.request.RequestFormAssembler;
import wooteco.subway.controller.dto.request.StationRequestForm;
import wooteco.subway.controller.dto.response.ResponseFormAssembler;
import wooteco.subway.controller.dto.response.StationResponseForm;
import wooteco.subway.exception.DuplicateStationNameException;
import wooteco.subway.service.StationService;
import wooteco.subway.service.dto.request.StationRequest;
import wooteco.subway.service.dto.response.StationResponse;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;
    private final RequestFormAssembler requestFormAssembler;
    private final ResponseFormAssembler responseFormAssembler;

    public StationController(StationService stationService,
                             RequestFormAssembler requestFormAssembler,
                             ResponseFormAssembler responseFormAssembler) {
        this.stationService = stationService;
        this.requestFormAssembler = requestFormAssembler;
        this.responseFormAssembler = responseFormAssembler;
    }

    @PostMapping
    public ResponseEntity<StationResponseForm> create(@RequestBody StationRequestForm stationRequestForm) {
        StationRequest stationRequest = requestFormAssembler.stationRequest(stationRequestForm);
        StationResponse stationResponse = stationService.create(stationRequest);
        StationResponseForm stationResponseForm = responseFormAssembler.stationResponseForm(stationResponse);
        URI redirectUri = URI.create("/stations/" + stationResponseForm.getId());
        return ResponseEntity.created(redirectUri).body(stationResponseForm);
    }

    @GetMapping
    public ResponseEntity<List<StationResponseForm>> showStations() {
        List<StationResponse> stationResponses = stationService.findAll();
        List<StationResponseForm> stationResponseForms = responseFormAssembler.stationResponseForms(stationResponses);
        return ResponseEntity.ok(stationResponseForms);
    }

    @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long stationId) {
        stationService.remove(stationId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(DuplicateStationNameException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
