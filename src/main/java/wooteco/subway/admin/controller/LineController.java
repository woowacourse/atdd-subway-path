package wooteco.subway.admin.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.service.LineService;

@RestController
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping(value = "/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody @Valid LineRequest lineRequest) {
        Line persistLine = lineService.save(lineRequest.toLine());

        return ResponseEntity
                .created(URI.create("/lines/" + persistLine.getId()))
                .body(LineResponse.of(persistLine));
    }

    @GetMapping("/lines")
    public ResponseEntity<List<LineResponse>> showLine() {
        List<LineResponse> lineResponses = LineResponse.listOf(lineService.showLines());
        return ResponseEntity.ok()
                .eTag(String.valueOf(lineResponses.hashCode()))
                .body(lineResponses);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineDetailResponse> retrieveLine(@PathVariable Long id) {
        LineDetailResponse lineDetailResponse = lineService.findLineWithStationsById(id);
        return ResponseEntity.ok()
                .eTag(String.valueOf(lineDetailResponse.hashCode()))
                .body(lineDetailResponse);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody @Valid LineRequest lineRequest) {
        lineService.updateLine(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lines/{lineId}/stations")
    public ResponseEntity<Void> addLineStation(@PathVariable Long lineId,
            @RequestBody @Valid LineStationCreateRequest lineStationCreateRequest) {
        lineService.addLineStation(lineId, lineStationCreateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> removeLineStation(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineService.removeLineStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/lines/details")
    public ResponseEntity<WholeSubwayResponse> getWholeSubway() {
        WholeSubwayResponse wholeSubwayResponse = lineService.wholeLines();
        return ResponseEntity.ok()
                .eTag(String.valueOf(wholeSubwayResponse.hashCode()))
                .body(wholeSubwayResponse);
    }
}
