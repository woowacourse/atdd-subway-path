package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.station.StationRequest;
import wooteco.subway.exception.IllegalInputException;
import wooteco.subway.exception.station.DuplicateStationException;
import wooteco.subway.exception.station.NoSuchStationException;

@Service
@Transactional
public class StationService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public StationService(final StationDao stationDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public Station create(final StationRequest request) {
        final Station station = new Station(request.getName());
        return stationDao.insert(station)
                .orElseThrow(DuplicateStationException::new);
    }

    @Transactional(readOnly = true)
    public Station findById(final Long id) {
        return stationDao.findById(id)
                .orElseThrow(NoSuchStationException::new);
    }

    @Transactional(readOnly = true)
    public List<Station> findAll() {
        return stationDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Station> findAllByLineId(final Long lineId) {
        return stationDao.findAllByLineId(lineId);
    }

    public void delete(final Long id) {
        if (sectionDao.existStation(id)) {
            throw new IllegalInputException("역이 구간에 등록되어 있습니다.");
        }
        stationDao.deleteById(id);
    }
}
