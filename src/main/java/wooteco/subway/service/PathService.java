package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.line.LineEntity;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.factory.JgraphtPathFactory;
import wooteco.subway.domain.path.factory.PathFactory;
import wooteco.subway.domain.path.strategy.DijkstraStrategy;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.SectionEntity;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public PathService(StationDao stationDao, SectionDao sectionDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public PathResponse searchPath(PathRequest pathRequest) {
        Path shortestPath = getShortestPath(pathRequest);
        List<Station> shortestPathStations = shortestPath.getStations();
        int distance = shortestPath.calculateDistance();
        int fare = getFare(shortestPath, distance, pathRequest.getAge());

        return new PathResponse(createStationResponseOf(shortestPathStations), distance, fare);
    }

    private Path getShortestPath(PathRequest pathRequest) {
        Station sourceStation = stationDao.findById(pathRequest.getSource());
        Station targetStation = stationDao.findById(pathRequest.getTarget());

        PathFactory pathFactory = JgraphtPathFactory.of(findAllSections(), new DijkstraStrategy());
        return Path.of(pathFactory, sourceStation, targetStation);
    }

    private int getFare(Path path, int distance, int age) {
        Map<Long, Integer> lineExtraFares = lineDao.findAll()
                .stream()
                .collect(Collectors.toMap(LineEntity::getId, LineEntity::getExtraFare));

        int extraFare = path.getPathExtraFare(lineExtraFares);
        Fare fare = Fare.of(distance, age, extraFare);
        return fare.calculateFare();
    }

    private List<Section> findAllSections() {
        List<SectionEntity> sectionEntities = sectionDao.findAll();

        return sectionEntities.stream()
                .map(this::assembleSection)
                .collect(Collectors.toList());
    }

    private Section assembleSection(SectionEntity sectionEntity) {
        Station upStation = stationDao.findById(sectionEntity.getUpStationId());
        Station downStation = stationDao.findById(sectionEntity.getDownStationId());

        return new Section(sectionEntity.getId(), sectionEntity.getLineId(),
                upStation, downStation, sectionEntity.getDistance());
    }

    private List<StationResponse> createStationResponseOf(List<Station> path) {
        return path.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }
}
