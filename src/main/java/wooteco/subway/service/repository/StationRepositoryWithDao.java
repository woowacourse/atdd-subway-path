package wooteco.subway.service.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;

@Repository
public class StationRepositoryWithDao implements StationRepository {
    private static final String ERROR_MESSAGE_DUPLICATE_NAME = "중복된 지하철 역 이름입니다.";
    private static final String ERROR_MESSAGE_NOT_EXISTS_ID = "존재하지 않는 지하철 역입니다.";
    private static final String ERROR_MESSAGE_ALREADY_USED = "해당 역을 지나는 노선이 있으므로 삭제가 불가합니다.";
    private static final String ERROR_MESSAGE_NOT_EXISTS_STATION = "존재하지 않는 역을 지나는 구간은 만들 수 없습니다.";

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public StationRepositoryWithDao(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Override
    public Station save(Station station) {
        validateNameDuplicate(station.getName());
        return stationDao.save(station);
    }

    private void validateNameDuplicate(String name) {
        if (stationDao.existByName(name)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_DUPLICATE_NAME);
        }
    }

    @Override
    public List<Station> findAll() {
        return stationDao.findAll();
    }

    @Override
    public Station find(long id) {
        validateNotExistStation(id);
        return stationDao.getStation(id);
    }

    private void validateNotExistStation(long id) {
        if (!stationDao.existById(id)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_EXISTS_STATION);
        }
    }

    @Override
    public void delete(long id) {
        validateNotExists(id);
        validateAlreadyUsedInSection(id);
        stationDao.delete(id);
    }

    private void validateNotExists(Long id) {
        if (!stationDao.existById(id)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_EXISTS_ID);
        }
    }

    private void validateAlreadyUsedInSection(Long id) {
        if (sectionDao.existSectionUsingStation(id)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ALREADY_USED);
        }
    }
}
