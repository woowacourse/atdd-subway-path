package wooteco.subway.admin.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import wooteco.subway.admin.repository.StationRepository;
import wooteco.subway.admin.service.LineService;

/**
 *    class description
 *
 *    @author HyungJu An, Kuenhwi Choi
 */
@Controller
public class ServicePageController {
	private final LineService lineService;
	private final StationRepository stationRepository;

	public ServicePageController(final LineService lineService,
		final StationRepository stationRepository) {
		this.lineService = lineService;
		this.stationRepository = stationRepository;
	}

	@GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
	public String index() {
		return "service/index";
	}

	@GetMapping(value = "/map", produces = MediaType.TEXT_HTML_VALUE)
	public String mapPage(Model model) {
		model.addAttribute("stations", stationRepository.findAll());
		return "service/map";
	}

	@GetMapping(value = "/search", produces = MediaType.TEXT_HTML_VALUE)
	public String searchPage(Model model) {
		model.addAttribute("lines", lineService.showLines());
		return "service/search";
	}
}
