package wooteco.subway.admin.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.WholeSubwayResponse;
import wooteco.subway.admin.service.LineService;

@RestController
public class ClientController {
    private LineService lineService;

    public ClientController(LineService lineService) {
        this.lineService = lineService;
    }

    @GetMapping("/lines/detail")
    public ResponseEntity<WholeSubwayResponse> retrieveLines() {
        WholeSubwayResponse wholeSubwayResponse = lineService.wholeLines();
        return ResponseEntity.ok()
            .eTag(String.valueOf(wholeSubwayResponse.hashCode()))
            .body(wholeSubwayResponse);
    }
}
