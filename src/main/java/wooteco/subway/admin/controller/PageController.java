package wooteco.subway.admin.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import wooteco.subway.admin.repository.StationRepository;
import wooteco.subway.admin.service.LineService;

@Controller
public class PageController {
    private LineService lineService;
    private StationRepository stationRepository;

    public PageController(LineService lineService, StationRepository stationRepository) {
        this.lineService = lineService;
        this.stationRepository = stationRepository;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return "admin/index";
    }

    @GetMapping(value = "/stations", produces = MediaType.TEXT_HTML_VALUE)
    public String stationPage(Model model) {
        model.addAttribute("stations", stationRepository.findAll());
        return "admin/admin-station";
    }

    @GetMapping(value = "/lines", produces = MediaType.TEXT_HTML_VALUE)
    public String linePage(Model model) {
        model.addAttribute("lines", lineService.showLines());
        return "admin/admin-line";
    }

    @GetMapping(value = "/edges", produces = MediaType.TEXT_HTML_VALUE)
    public String edgePage() {
        return "admin/admin-edge";
    }

    @GetMapping(value = "/service", produces = MediaType.TEXT_HTML_VALUE)
    public String serviceIndexPage() {
        return "service/index";
    }

    @GetMapping(value = "/search", produces = MediaType.TEXT_HTML_VALUE)
    public String serviceSearchPage() {
        return "service/search";
    }

    @GetMapping(value = "/map", produces = MediaType.TEXT_HTML_VALUE)
    public String serviceMapPage() {
        return "service/map";
    }

    @GetMapping(value = "/login", produces = MediaType.TEXT_HTML_VALUE)
    public String serviceLogInPage() {
        return "service/login";
    }

    @GetMapping(value = "/join", produces = MediaType.TEXT_HTML_VALUE)
    public String serviceJoinPage() {
        return "service/join";
    }

    @GetMapping(value = "/mypage", produces = MediaType.TEXT_HTML_VALUE)
    public String serviceMyPage() {
        return "service/mypage";
    }

    @GetMapping(value = "/mypage-edit", produces = MediaType.TEXT_HTML_VALUE)
    public String serviceMyPageEditPage() {
        return "service/mypage-edit";
    }
}
