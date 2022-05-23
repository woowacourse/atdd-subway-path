package wooteco.subway.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.section.Section;
import wooteco.subway.repository.SectionsDirtyChecker;

@Service
@Transactional(readOnly = true)
public class LineService {

	private final LineRepository lineRepository;
	private final SectionRepository sectionRepository;

	public LineService(LineRepository lineRepository, SectionRepository sectionRepository) {
		this.lineRepository = lineRepository;
		this.sectionRepository = sectionRepository;
	}

	@Transactional
	public Line create(String name, String color, Section section, int extraFare) {
		validateNameNotDuplicated(name);
		Long lineId = lineRepository.save(new Line(name, color, extraFare, List.of(section)));
		return lineRepository.findById(lineId);
	}

	private void validateNameNotDuplicated(String name) {
		if (lineRepository.existsByName(name)) {
			throw new IllegalArgumentException("해당 이름의 지하철 노선이 이미 존재합니다");
		}
	}

	public List<Line> listLines() {
		return lineRepository.findAll();
	}

	public Line findOne(Long id) {
		return lineRepository.findById(id);
	}

	@Transactional
	public Line update(Line line) {
		lineRepository.update(line);
		return findOne(line.getId());
	}

	@Transactional
	public void remove(Long id) {
		lineRepository.remove(id);
	}

	@Transactional
	public void addSection(Long id, Section section) {
		Line line = lineRepository.findById(id);
		SectionsDirtyChecker checker = SectionsDirtyChecker.from(line.getSections());

		line.addSection(section);

		executeDirtyChecking(line, checker);
	}

	@Transactional
	public void deleteSection(Long lineId, Long stationId) {
		Line line = lineRepository.findById(lineId);
		SectionsDirtyChecker checker = SectionsDirtyChecker.from(line.getSections());

		line.deleteSectionByStation(stationId);

		executeDirtyChecking(line, checker);
	}

	private void executeDirtyChecking(Line line, SectionsDirtyChecker checker) {
		List<Section> sections = line.getSections();
		sectionRepository.saveAll(line.getId(), checker.findSaved(sections));
		sectionRepository.updateAll(checker.findUpdated(sections));
		sectionRepository.removeAll(checker.findDeleted(sections));
	}
}
