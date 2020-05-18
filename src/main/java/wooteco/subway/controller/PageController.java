package wooteco.subway.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import wooteco.subway.repository.StationRepository;
import wooteco.subway.service.admin.LineService;

@Controller
public class PageController {
	private final LineService lineService;
	private final StationRepository stationRepository;

	public PageController(LineService lineService, StationRepository stationRepository) {
		this.lineService = lineService;
		this.stationRepository = stationRepository;
	}

	@GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
	public String clientIndex() {
		return "service/index";
	}

	@GetMapping(value = "/map", produces = MediaType.TEXT_HTML_VALUE)
	public String mapPage() {
		return "service/map";
	}

	@GetMapping(value = "/search", produces = MediaType.TEXT_HTML_VALUE)
	public String searchPage() {
		return "service/search";
	}

	@GetMapping(value = "/admin", produces = MediaType.TEXT_HTML_VALUE)
	public String adminIndex() {
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
	public String edgePage(Model model) {
		return "admin/admin-edge";
	}
}
