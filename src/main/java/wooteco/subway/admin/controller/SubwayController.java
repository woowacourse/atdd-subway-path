package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.service.LineService;

@RestController
public class SubwayController {

    private LineService lineService;

    public SubwayController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping("/lines/stations")
    public ResponseEntity<WholeSubwayResponse> showSubways() {
        return ResponseEntity.ok().body(lineService.wholeLines());
    }
}
