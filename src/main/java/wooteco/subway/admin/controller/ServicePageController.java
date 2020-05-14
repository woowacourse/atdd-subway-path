package wooteco.subway.admin.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import wooteco.subway.admin.repository.StationRepository;
import wooteco.subway.admin.service.LineService;

@Controller
public class ServicePageController {
    private LineService lineService;
    private StationRepository stationRepository;

    public ServicePageController(LineService lineService,
                                 StationRepository stationRepository) {
        this.lineService = lineService;
        this.stationRepository = stationRepository;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return "service/index";
    }

    @GetMapping(value = "/search", produces = MediaType.TEXT_HTML_VALUE)
    public String searchPage() {
        return "service/search";
    }

}
