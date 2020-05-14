package wooteco.subway.admin.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.dto.request.LineRequest;
import wooteco.subway.admin.dto.request.LineStationCreateRequest;
import wooteco.subway.admin.dto.response.LineDetailResponse;
import wooteco.subway.admin.dto.response.LineResponse;
import wooteco.subway.admin.dto.response.WholeSubwayResponse;
import wooteco.subway.admin.service.LineService;

@RestController
@RequestMapping("/lines")
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest view) {
        Line persistLine = lineService.save(view.toLine());

        return ResponseEntity
            .created(URI.create("/lines/" + persistLine.getId()))
            .body(LineResponse.of(persistLine));
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLine() {
        return ResponseEntity.ok().body(LineResponse.listOf(lineService.showLines()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineDetailResponse> retrieveLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(lineService.findLineWithStationsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateLine(@PathVariable Long id, @RequestBody LineRequest view) {
        lineService.updateLine(id, view);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/detail")
    public ResponseEntity<WholeSubwayResponse> retrieveWholeSubway() {
        WholeSubwayResponse wholeSubwayResponse = lineService.wholeLines();
        return ResponseEntity.ok().body(wholeSubwayResponse);
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity addLineStation(@PathVariable Long lineId,
        @RequestBody LineStationCreateRequest view) {
        lineService.addLineStation(lineId, view);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity removeLineStation(@PathVariable Long lineId,
        @PathVariable Long stationId) {
        lineService.removeLineStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
