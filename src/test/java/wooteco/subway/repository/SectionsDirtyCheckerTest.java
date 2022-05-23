package wooteco.subway.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.line.section.Section;
import wooteco.subway.domain.line.section.Sections;
import wooteco.subway.domain.Station;

class SectionsDirtyCheckerTest {

	private final Station station1 = new Station(1L, "강남역");
	private final Station station2 = new Station(2L, "역삼역");
	private final Station station3 = new Station(3L, "선릉역");

	private final Section section1 = new Section(1L, station1, station2, 10);
	private final Section section2 = new Section(2L, station2, station3, 10);
	private final Sections sections = new Sections(List.of(section1, section2));

	@DisplayName("수정된 Section만 골라서 찾는다.")
	@Test
	void findUpdated() {
		SectionsDirtyChecker checker = SectionsDirtyChecker.from(sections.getValues());

		Station newStation = new Station(4L, "교대역");
		sections.add(new Section(3L, station1, newStation, 5));

		List<Section> updated = checker.findUpdated(sections.getValues());

		assertAll(
			() -> assertThat(updated).hasSize(1),
			() -> assertThat(updated.get(0))
				.isEqualTo(new Section(1L,
					new Station(4L, "교대역"), new Station(2L, "역삼역"), 5)
				)
		);
	}

	@DisplayName("삭제된 Section만 골라서 찾는다.")
	@Test
	void findDeleted() {
		SectionsDirtyChecker checker = SectionsDirtyChecker.from(sections.getValues());

		sections.deleteByStation(2L);

		List<Section> deleted = checker.findDeleted(sections.getValues());
		assertAll(
			() -> assertThat(deleted).hasSize(2),
			() -> assertThat(deleted)
				.containsOnly(section1, section2)
		);
	}

	@DisplayName("저장된 Section만 골라서 찾는다.")
	@Test
	void findSaved() {
		SectionsDirtyChecker checker = SectionsDirtyChecker.from(sections.getValues());

		sections.deleteByStation(2L);

		List<Section> saved = checker.findSaved(sections.getValues());
		assertAll(
			() -> assertThat(saved).hasSize(1),
			() -> assertThat(saved)
				.containsOnly(new Section(0L, station1, station3, 20))
		);
	}
}
