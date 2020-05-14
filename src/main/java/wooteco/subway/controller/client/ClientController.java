package wooteco.subway.controller.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.WholeSubwayResponse;
import wooteco.subway.service.client.ClientService;

@RestController
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
    public ResponseEntity<PathResponse> searchPathByShortestDistance(String source, String target) {
        PathResponse pathResponse = clientService.searchPathByShortestDistance(source, target);
        return ResponseEntity.ok().body(pathResponse);
    }

}
