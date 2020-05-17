package wooteco.subway.admin.common.config;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import wooteco.subway.admin.line.service.LineService;
import wooteco.subway.admin.station.repository.StationRepository;

@RequestMapping("/admin")
@Controller
public class AdminPageController {

	private LineService lineService;
	private StationRepository stationRepository;

	public AdminPageController(LineService lineService, StationRepository stationRepository) {
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
	public String edgePage(Model model) {
		return "admin/admin-edge";
	}
}
