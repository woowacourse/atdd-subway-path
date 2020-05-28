package wooteco.subway.admin.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.request.StationCreateRequest;
import wooteco.subway.admin.dto.response.StandardResponse;
import wooteco.subway.admin.dto.response.StationResponse;
import wooteco.subway.admin.service.StationService;

@RestController
@RequestMapping("/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public StandardResponse<List<StationResponse>> showStations() {
        return StandardResponse.of(stationService.showStations());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StandardResponse<StationResponse> createStation(@RequestBody @Valid StationCreateRequest request) {
        StationResponse response = stationService.createStation(request);

        return StandardResponse.of(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public StandardResponse<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStation(id);
        return StandardResponse.empty();
    }
}
