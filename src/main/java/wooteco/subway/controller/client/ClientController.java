package wooteco.subway.controller.client;

import javax.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.WholeSubwayResponse;
import wooteco.subway.service.client.ClientService;

@RestController
@Validated
public class ClientController {
    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/lines/detail")
    public ResponseEntity<WholeSubwayResponse> retrieveLines() {
        WholeSubwayResponse wholeSubwayResponse = clientService.wholeLines();
        return ResponseEntity.ok()
            .eTag(String.valueOf(wholeSubwayResponse.hashCode()))
            .body(wholeSubwayResponse);
    }

    @GetMapping("/lines/path")
    public ResponseEntity<PathResponse> searchPathByShortestDistance(@NotBlank String source,
        @NotBlank String target, @NotBlank String type) {
        PathResponse pathResponse = clientService.searchPath(source, target, type);
        return ResponseEntity.ok().body(pathResponse);
    }

}

