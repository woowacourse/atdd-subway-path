package wooteco.subway.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import wooteco.subway.repository.dao.SectionDao;
import wooteco.subway.repository.table.SectionTable;
import wooteco.subway.domain.line.section.Section;

@Repository
public class JdbcSectionRepository implements SectionRepository {

	private final SectionDao sectionDao;
	private final StationRepository stationRepository;

	public JdbcSectionRepository(SectionDao sectionDao, StationRepository stationRepository) {
		this.sectionDao = sectionDao;
		this.stationRepository = stationRepository;
	}

	@Override
	public Long save(Long lineId, Section section) {
		return sectionDao.save(SectionTable.of(lineId, section));
	}

	@Override
	public void saveAll(Long lineId, List<Section> sections) {
		sectionDao.saveAll(
			sections.stream()
				.map(section -> SectionTable.of(lineId, section))
				.collect(Collectors.toList())
		);
	}

	@Override
	public Section findById(Long id) {
		SectionTable sectionTable = sectionDao.findById(id);
		return sectionTable.toEntity(
			stationRepository.findById(sectionTable.getUpStationId()),
			stationRepository.findById(sectionTable.getDownStationId())
		);
	}

	@Override
	public List<Section> findByLineId(Long lineId) {
		return sectionDao.findByLineId(lineId)
			.stream()
			.map(table -> table.toEntity(
				stationRepository.findById(table.getUpStationId()),
				stationRepository.findById(table.getDownStationId())
			)).collect(Collectors.toList());
	}

	@Override
	public void update(Section section) {
		sectionDao.update(SectionTable.from(section));
	}

	@Override
	public void updateAll(List<Section> sections) {
		sectionDao.updateAll(
			sections.stream()
				.map(SectionTable::from)
				.collect(Collectors.toList())
		);
	}

	@Override
	public void remove(Long id) {
		sectionDao.remove(id);
	}

	@Override
	public void removeAll(List<Section> sections) {
		sectionDao.removeAll(sections.stream()
			.map(Section::getId)
			.collect(Collectors.toList())
		);
	}

	@Override
	public boolean existByStation(Long stationId) {
		return sectionDao.existByStationId(stationId);
	}
}
