package wooteco.subway.admin.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import wooteco.subway.admin.service.LineService;
import wooteco.subway.admin.service.StationService;

@Controller
public class AdminPageController {
    private final LineService lineService;
    private final StationService stationService;

    public AdminPageController(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @GetMapping(value = "/stations", produces = MediaType.TEXT_HTML_VALUE)
    public String stationPage(Model model) {
        model.addAttribute("stations", stationService.findAll());
        return "admin/admin-station";
    }

    @GetMapping(value = "/lines", produces = MediaType.TEXT_HTML_VALUE)
    public String linePage(Model model) {
        model.addAttribute("lines", lineService.showLines());
        return "admin/admin-line";
    }

    @GetMapping(value = "/edges", produces = MediaType.TEXT_HTML_VALUE)
    public String edgePage(Model model) {
        return "admin/admin-edge";
    }
}
