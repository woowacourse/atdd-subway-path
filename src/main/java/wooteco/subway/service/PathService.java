package wooteco.subway.service;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;

@Service
public class PathService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(Long upStationId, Long downStationId) {
        validateNotSameStations(upStationId, downStationId);
        Path path = getPath();

        final Station upStation = findStation(upStationId);
        final Station downStation = findStation(downStationId);

        final List<Station> stations = path.getStations(upStation, downStation);
        final int shortestDistance = path.getShortestDistance(upStation, downStation);
        final Fare fare = Fare.from(shortestDistance);
        return new PathResponse(stations, shortestDistance, fare.getValue());
    }

    private Path getPath() {
        Path path = new Path();
        path.addAllStations(stationDao.findAll());
        addAllSections(path);
        return path;
    }

    private void validateNotSameStations(Long upStationId, Long downStationId) {
        if (Objects.equals(upStationId, downStationId)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }
    }

    private void addAllSections(Path graph) {
        final List<Section> sections = sectionDao.findAll();
        for (Section section : sections) {
            final Station upStation = findStation(section.getUpStationId());
            final Station downStation = findStation(section.getDownStationId());
            graph.addSection(upStation, downStation, section.getDistance());
        }
    }

    private Station findStation(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 역이 존재하지 않습니다."));
    }
}
