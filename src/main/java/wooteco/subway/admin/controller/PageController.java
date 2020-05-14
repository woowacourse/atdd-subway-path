package wooteco.subway.admin.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public class PageController {

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping(value = "/service")
    public String serviceIndex() {
        return "service/index";
    }

    @GetMapping(value = "/service/map")
    public String map() {
        return "service/map";
    }

    @GetMapping(value = "/service/search")
    public String search() {
        return "service/search";
    }

    @GetMapping(value = "/admin")
    public String adminIndex() {
        return "admin/index";
    }

    @GetMapping(value = "/admin/station")
    public String stationPage() {
        return "admin/admin-station";
    }

    @GetMapping(value = "/admin/line")
    public String linePage() {
        return "admin/admin-line";
    }

    @GetMapping(value = "/admin/edge")
    public String edgePage() {
        return "admin/admin-edge";
    }
}
