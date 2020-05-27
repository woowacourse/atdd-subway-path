package wooteco.subway.admin.controller.restcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.request.LineStationCreateRequest;
import wooteco.subway.admin.dto.request.PathRequest;
import wooteco.subway.admin.dto.response.LineDetailResponse;
import wooteco.subway.admin.dto.response.PathResponse;
import wooteco.subway.admin.dto.response.WholeSubwayResponse;
import wooteco.subway.admin.service.PathService;

@RestController
@RequestMapping("/paths")
public class PathController {
    private PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(PathRequest pathRequest) {
        PathResponse response = pathService.findPath(pathRequest.getSource(),
            pathRequest.getTarget(), pathRequest.getType());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lines/detail")
    public ResponseEntity<WholeSubwayResponse> retrieveWholeSubway() {
        WholeSubwayResponse wholeSubwayResponse = pathService.wholeLines();
        return ResponseEntity.ok().body(wholeSubwayResponse);
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineDetailResponse> retrieveLine(@PathVariable Long id) {
        return ResponseEntity.ok().body(pathService.findLineWithStationsById(id));
    }

    @PostMapping("/lines/{lineId}/stations")
    public ResponseEntity<Void> addLineStation(@PathVariable Long lineId,
        @RequestBody LineStationCreateRequest view) {
        pathService.addLineStation(lineId, view);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> removeLineStation(@PathVariable Long lineId,
        @PathVariable Long stationId) {
        pathService.removeLineStation(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}
