package wooteco.subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dto.SectionDto;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public SectionRepository(final SectionDao sectionDao, final LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public void saveSection(final Section section) {
        sectionDao.save(section.getLineId(), section.getUpStationId(), section.getDownStationId(),
                section.getDistance());
    }

    public void deleteById(final long lineId, final long stationId) {
        sectionDao.deleteById(lineId, stationId);
    }

    public void deleteByLineId(long lineId) {
        sectionDao.deleteByLineId(lineId);
    }

    public List<Section> getSectionsByLineId(long lineId) {
        List<SectionDto> sectionDtos = sectionDao.findAllByLineId(lineId);

        return sectionDtos.stream()
                .map(sectionDto -> toEntity(sectionDto))
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        List<SectionDto> sectionDtos = sectionDao.findAll();

        return sectionDtos.stream()
                .map(sectionDto -> toEntity(sectionDto))
                .collect(Collectors.toList());
    }


    public Section toEntity(SectionDto sectionDto) {
        return new Section(sectionDto.getId(),
                lineDao.getById(sectionDto.getLineId()), sectionDto.getUpStationId(), sectionDto.getDownStationId(),
                        sectionDto.getDistance());
    }
}
