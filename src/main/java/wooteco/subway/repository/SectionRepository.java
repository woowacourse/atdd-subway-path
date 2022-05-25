package wooteco.subway.repository;

import java.util.List;

import wooteco.subway.domain.line.section.Section;

public interface SectionRepository {
	Long save(Long lineId, Section section);

	void saveAll(Long lineId, List<Section> sections);

	Section findById(Long id);

	List<Section> findByLineId(Long lineId);

	void update(Section section);

	void updateAll(List<Section> sections);

	void remove(Long id);

	void removeAll(List<Section> sections);

	boolean existByStation(Long stationId);
}
