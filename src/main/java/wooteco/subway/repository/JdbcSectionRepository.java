package wooteco.subway.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import wooteco.subway.repository.dao.SectionDao;
import wooteco.subway.repository.table.SectionTable;
import wooteco.subway.domain.Section;

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
	public void remove(Long id) {
		sectionDao.remove(id);
	}

	@Override
	public boolean existByStation(Long stationId) {
		return sectionDao.existByStationId(stationId);
	}
}
