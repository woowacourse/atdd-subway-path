package wooteco.subway.repository;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.line.section.Section;

public class SectionsDirtyChecker {

	private final List<Section> snapShot;

	public SectionsDirtyChecker(List<Section> snapShot) {
		this.snapShot = snapShot;
	}

	public static SectionsDirtyChecker from(List<Section> sections) {
		List<Section> snapshot = sections.stream()
			.map(section -> new Section(
				section.getId(),
				section.getUpStation(),
				section.getDownStation(),
				section.getDistance()))
			.collect(Collectors.toList());
		return new SectionsDirtyChecker(snapshot);
	}

	public List<Section> findUpdated(List<Section> sections) {
		return sections.stream()
			.filter(updated -> snapShot.stream()
				.anyMatch(origin -> origin.isSameId(updated) && !origin.equals(updated))
			).collect(Collectors.toList());
	}

	public List<Section> findDeleted(List<Section> sections) {
		List<Long> ids = sections.stream()
			.map(Section::getId)
			.collect(Collectors.toList());
		return snapShot.stream()
			.filter(origin -> !ids.contains(origin.getId()))
			.collect(Collectors.toList());
	}

	public List<Section> findSaved(List<Section> sections) {
		return sections.stream()
			.filter(updated -> !updated.hasId())
			.collect(Collectors.toList());
	}
}
