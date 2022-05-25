package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.service.request.SectionCreateRequest;
import wooteco.subway.dto.service.request.SectionDeleteRequest;

@Service
public class SectionService {
    private static final String ERROR_MESSAGE_NOT_EXISTS_STATION = "존재하지 않는 역을 지나는 구간은 만들 수 없습니다.";

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineRepository lineRepository;

    public SectionService(LineRepository lineRepository, SectionDao sectionDao, StationDao stationDao) {
        this.lineRepository = lineRepository;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Transactional
    public void save(SectionCreateRequest sectionCreateRequest) {
        long upStationId = sectionCreateRequest.getUpStationId();
        long downStationId = sectionCreateRequest.getDownStationId();

        validateBeforeSave(upStationId, downStationId);

        Section section = new Section(stationDao.getStation(upStationId), stationDao.getStation(downStationId),
            sectionCreateRequest.getDistance());
        Line line = lineRepository.find(sectionCreateRequest.getLineId());
        line.updateToAdd(section);

        sectionDao.save(line.getId(), section);
        saveUpdatedLine(line);
    }

    private void saveUpdatedLine(Line line) {
        Sections sections = line.getSections();
        sections.forEach(section1 -> sectionDao.update(line.getId(), section1));
    }

    private void validateBeforeSave(long upStationId, long downStationId) {
        validateNotExistStation(upStationId);
        validateNotExistStation(downStationId);
    }

    @Transactional
    public void delete(SectionDeleteRequest sectionDeleteRequest) {
        long lineId = sectionDeleteRequest.getLineId();
        long stationId = sectionDeleteRequest.getStationId();

        validateNotExistStation(stationId);

        Line line = lineRepository.find(lineId);
        Station station = stationDao.getStation(stationId);
        Section deletedSection = line.delete(station);

        sectionDao.delete(lineId, deletedSection);
        line.getSections().forEach(section -> sectionDao.update(lineId, section));
    }

    private void validateNotExistStation(long id) {
        if (!stationDao.existById(id)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_EXISTS_STATION);
        }
    }
}
