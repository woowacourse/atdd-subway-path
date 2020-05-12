package wooteco.subway.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PathController {
	@GetMapping("/")
	public String serviceIndex() {
		return "service/index";
	}

	@GetMapping("/map-page")
	public String mapPage() {
		return "service/map";
	}

	@GetMapping("/search-page")
	public String searchPage() {
		return "service/search";
	}
}
