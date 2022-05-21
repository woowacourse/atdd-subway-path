package wooteco.subway.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.AgePolicy;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.service.PathServiceRequest;
import wooteco.subway.dto.service.PathServiceResponse;
import wooteco.subway.dto.service.StationDto;
import wooteco.subway.entity.LineEntity;
import wooteco.subway.entity.SectionEntity;
import wooteco.subway.strategy.DistanceFareStrategy;
import wooteco.subway.strategy.FareStrategy;
import wooteco.subway.utils.DijkstraShortestPathStation;

@Service
public class PathService {

    private static final String ERROR_MESSAGE_NOT_EXISTS_ID = "존재하지 않는 지하철 역입니다.";

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public PathService(StationDao stationDao, SectionDao sectionDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public PathServiceResponse getShortestPath(PathServiceRequest pathServiceRequest) {
        validateExists(pathServiceRequest.getSource());
        validateExists(pathServiceRequest.getTarget());

        Station source = stationDao.getStation(pathServiceRequest.getSource());
        Station target = stationDao.getStation(pathServiceRequest.getTarget());

        List<SectionEntity> sectionEntities = sectionDao.findAll();
        Sections sections = getSections(sectionEntities);
        Path path = DijkstraShortestPathStation.getPath(sections, source, target);
        List<StationDto> stationDtos = path.getStations().stream()
                .map(station -> new StationDto(station.getId(), station.getName()))
                .collect(Collectors.toList());
        List<Long> visitLines = getVisitLines(path.getStations(), sectionEntities);
        int extraFare = getExtraFare(visitLines);

        AgePolicy agePolicy = AgePolicy.fromAge(pathServiceRequest.getAge());
        int fare = new DistanceFareStrategy().calculate(path.getDistance()) + extraFare;
        fare = agePolicy.getDiscountedFare(fare);
        return new PathServiceResponse(stationDtos, path.getDistance(), fare);
    }

    private Sections getSections(List<SectionEntity> sectionEntities) {
        return new Sections(sectionEntities.stream()
                .map(sectionEntity -> new Section(
                        sectionEntity.getId(),
                        stationDao.getStation(sectionEntity.getUpStationId()),
                        stationDao.getStation(sectionEntity.getDownStationId()),
                        sectionEntity.getDistance()))
                .collect(Collectors.toList()));
    }


    private List<Long> getVisitLines(List<Station> stations, List<SectionEntity> sectionEntities) {
        Set<Long> lineIds = new HashSet<>();
        for (int i = 0; i < stations.size() - 1; i++) {
            Station upStation = stations.get(i);
            Station downStation = stations.get(i + 1);

            SectionEntity sectionEntity = sectionEntities.stream()
                    .filter(entity -> entity.getUpStationId() == upStation.getId())
                    .filter(entity -> entity.getDownStationId() == downStation.getId())
                    .min(Comparator.comparingInt(SectionEntity::getDistance))
                    .orElseThrow(() -> new IllegalStateException("구간 정보가 잘못되었습니다."));
            lineIds.add(sectionEntity.getLineId());
        }

        if (lineIds.isEmpty()) {
            throw new IllegalStateException("구간 정보가 잘못되었습니다.");
        }

        return new ArrayList<>(lineIds);
    }

    private int getExtraFare(List<Long> lineIds) {
        int maxExtraFare = 0;
        for (Long id : lineIds) {
            LineEntity lineEntity = lineDao.find(id);
            maxExtraFare = Math.max(maxExtraFare, lineEntity.getExtraFare());
        }
        return maxExtraFare;
    }

    private void validateExists(Long id) {
        if (!stationDao.existById(id)) {
            throw new NoSuchElementException(ERROR_MESSAGE_NOT_EXISTS_ID);
        }
    }
}
