package wooteco.subway.controller.map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.WholeSubwayResponse;
import wooteco.subway.service.map.MapService;

@RestController
public class MapController {
    private MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/map")
    public ResponseEntity<WholeSubwayResponse> retrieveLines() {
        WholeSubwayResponse wholeSubwayResponse = mapService.wholeLines();
        return ResponseEntity.ok()
            .eTag(String.valueOf(wholeSubwayResponse.hashCode()))
            .body(wholeSubwayResponse);
    }

    @GetMapping("/path")
    public ResponseEntity<PathResponse> searchPath(@Valid PathRequest request) {
        PathResponse pathResponse = mapService.searchPath(request.getSource(), request.getTarget(),
            request.getType());
        return ResponseEntity.ok().body(pathResponse);
    }
}

