package wooteco.subway.path.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.exception.RequestInvalidException;
import wooteco.subway.path.application.PathService;
import wooteco.subway.path.dto.PathRequest;
import wooteco.subway.path.dto.PathResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/paths")
public class PathController {
    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> searchPath(
            @ModelAttribute @Valid PathRequest pathRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RequestInvalidException();
        }
        PathResponse pathResponse = pathService.searchPath(pathRequest.getSource(), pathRequest.getTarget());
        return ResponseEntity.ok().body(pathResponse);
    }


}
