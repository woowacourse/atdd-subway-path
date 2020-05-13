package wooteco.subway.admin.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/service")
public class ServiceController {
    @GetMapping("/search")
    public String searchPage() {
        return "service/search";
    }

    @GetMapping("/map")
    public String mapPage() {
        return "service/map";
    }
}
