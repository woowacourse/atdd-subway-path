package wooteco.subway.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.repository.dao.LineDao;
import wooteco.subway.repository.dao.SectionDao;

@JdbcTest
class SectionRepositoryTest {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	@Autowired
	private DataSource dataSource;
	private SectionRepository sectionRepository;
	private LineRepository lineRepository;
	private StationRepository stationRepository;

	private Station upStation;
	private Station downStation;
	private Section section;
	private Long lineId;

	@BeforeEach
	void init() {
		stationRepository = new JdbcStationRepository(dataSource, jdbcTemplate);
		sectionRepository = new JdbcSectionRepository(new SectionDao(dataSource, jdbcTemplate), stationRepository);
		lineRepository = new JdbcLineRepository(new LineDao(dataSource, jdbcTemplate), sectionRepository);
		Long upStationId = stationRepository.save(new Station("강남역"));
		Long downStationId = stationRepository.save(new Station("역삼역"));
		upStation = new Station(upStationId, "강남역");
		downStation = new Station(downStationId, "역삼역");
		section = new Section(upStation, downStation, 10);
		lineId = lineRepository.save(new Line(0L, "2호선", "red"));
	}

	@DisplayName("지하철 구간을 저장한다.")
	@Test
	void save() {
		Long sectionId = sectionRepository.save(lineId, section);
		assertThat(sectionId).isGreaterThan(0);
	}

	@DisplayName("지하철 구간 여러 개를 한 번에 저장한다.")
	@Test
	void saveAll() {
		Long stationId = stationRepository.save(new Station("교대역"));
		Section newSection = new Section(downStation, new Station(stationId, "교대역"), 10);

		sectionRepository.saveAll(lineId, List.of(section, newSection));

		assertThat(sectionRepository.findByLineId(lineId)).hasSize(2);
	}

	@DisplayName("id로 지하철 구간을 조회한다.")
	@Test
	void findById() {
		Long sectionId = sectionRepository.save(lineId, section);

		Section foundSection = sectionRepository.findById(sectionId);
		assertThat(foundSection.getId()).isEqualTo(sectionId);
		assertThat(foundSection.getUpStationId()).isEqualTo(upStation.getId());
		assertThat(foundSection.getDownStationId()).isEqualTo(downStation.getId());
	}

	@DisplayName("지하철 노선 id로 구간을 조회한다.")
	@Test
	void findByLineId() {
		Long sectionId = sectionRepository.save(lineId, section);

		List<Section> sections = sectionRepository.findByLineId(lineId);
		Section foundSection = sections.get(0);
		assertThat(foundSection.getId()).isEqualTo(sectionId);
		assertThat(foundSection.getUpStationId()).isEqualTo(upStation.getId());
		assertThat(foundSection.getDownStationId()).isEqualTo(downStation.getId());
	}

	@DisplayName("구간을 수정한다.")
	@Test
	void updateSection() {
		Long sectionId = sectionRepository.save(lineId, section);

		Section updatedSection = new Section(sectionId, downStation, upStation, 7);
		sectionRepository.update(updatedSection);

		Section findSection = sectionRepository.findById(sectionId);
		assertThat(findSection).isEqualTo(updatedSection);
	}

	@DisplayName("구간을 여러 개 수정한다.")
	@Test
	void updateAllSection() {
		Long stationId = stationRepository.save(new Station("교대역"));
		Section newSection = new Section(downStation, new Station(stationId, "교대역"), 10);
		sectionRepository.saveAll(lineId, List.of(section, newSection));

		List<Section> sections = sectionRepository.findByLineId(lineId);

		sectionRepository.updateAll(
			sections.stream()
				.map(section -> new Section(
					section.getId(),
					section.getUpStation(),
					section.getDownStation(),
					7))
				.collect(Collectors.toList())
		);

		assertThat(sectionRepository.findByLineId(lineId))
			.map(Section::getDistance)
			.containsExactly(7, 7);
	}

	@DisplayName("구간을 삭제한다.")
	@Test
	void remove() {
		Long sectionId = sectionRepository.save(lineId, section);
		sectionRepository.remove(sectionId);

		assertThat(sectionRepository.findByLineId(lineId)).isEmpty();
	}

	@DisplayName("구간 여러 개를 한 번에 삭제한다.")
	@Test
	void removeAll() {
		Long stationId = stationRepository.save(new Station("교대역"));
		Section newSection = new Section(downStation, new Station(stationId, "교대역"), 10);
		sectionRepository.saveAll(lineId, List.of(section, newSection));

		sectionRepository.removeAll(sectionRepository.findByLineId(lineId));

		assertThat(sectionRepository.findByLineId(lineId)).hasSize(0);
	}

	@DisplayName("라인을 삭제하면 구간도 삭제된다.")
	@Test
	void removeLine() {
		sectionRepository.save(lineId, section);
		lineRepository.remove(lineId);
		List<Section> sections = sectionRepository.findByLineId(lineId);
		assertThat(sections).isEmpty();
	}

	@DisplayName("구간으로 등록된 역이 있는지 찾는다.")
	@Test
	void removeFailBySection() {
		Section section = new Section(upStation, downStation, 10);
		lineRepository.save(new Line(0L, "신분당선", "red", List.of(section)));
		assertAll(
			() -> assertThat(
				sectionRepository.existByStation(upStation.getId())).isTrue(),
			() -> assertThat(
				sectionRepository.existByStation(downStation.getId())).isTrue()
		);
	}
}
