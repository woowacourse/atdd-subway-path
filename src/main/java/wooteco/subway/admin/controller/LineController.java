package wooteco.subway.admin.controller;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.dto.request.LineRequest;
import wooteco.subway.admin.dto.request.LineStationCreateRequest;
import wooteco.subway.admin.dto.response.LineDetailResponse;
import wooteco.subway.admin.dto.response.LineResponse;
import wooteco.subway.admin.dto.response.StandardResponse;
import wooteco.subway.admin.dto.response.WholeSubwayResponse;
import wooteco.subway.admin.service.LineService;

@RestController
@RequestMapping("/lines")
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public StandardResponse<List<LineResponse>> showLine() {
        return StandardResponse.of(lineService.showLines());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StandardResponse<LineResponse> createLine(@RequestBody @Valid LineRequest request, HttpServletResponse response) {
        LineResponse lineResponse = lineService.save(request);

        response.addHeader("Location", "/lines/" + lineResponse.getId());
        return StandardResponse.of(lineResponse);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StandardResponse<LineDetailResponse> retrieveLine(@PathVariable Long id) {
        return StandardResponse.of(lineService.findLineWithStationsById(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StandardResponse<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest request) {
        lineService.updateLine(id, request);
        return StandardResponse.empty();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public StandardResponse<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return StandardResponse.empty();
    }

    @GetMapping("/detail")
    @ResponseStatus(HttpStatus.OK)
    public StandardResponse<WholeSubwayResponse> retrieveWholeSubway() {
        return StandardResponse.of(lineService.wholeLines());
    }

    @PostMapping("/{lineId}/stations")
    @ResponseStatus(HttpStatus.CREATED)
    public StandardResponse<Void> addLineStation(@PathVariable Long lineId, @RequestBody @Valid LineStationCreateRequest request) {
        lineService.addLineStation(lineId, request);
        return StandardResponse.empty();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public StandardResponse<Void> removeLineStation(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineService.removeLineStation(lineId, stationId);
        return StandardResponse.empty();
    }
}
