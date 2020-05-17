package wooteco.subway.controller.map;

import javax.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.WholeSubwayResponse;
import wooteco.subway.service.map.MapService;

@Validated
@RequestMapping("/map")
@RestController
public class MapController {
    private MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping
    public ResponseEntity<WholeSubwayResponse> retrieveLines() {
        WholeSubwayResponse wholeSubwayResponse = mapService.wholeLines();
        return ResponseEntity.ok()
            .eTag(String.valueOf(wholeSubwayResponse.hashCode()))
            .body(wholeSubwayResponse);
    }

    @GetMapping("/path")
    public ResponseEntity<PathResponse> searchPath(@NotBlank String source,
        @NotBlank String target, @NotBlank String type) {
        PathResponse pathResponse = mapService.searchPath(source, target, type);
        return ResponseEntity.ok().body(pathResponse);
    }

}

