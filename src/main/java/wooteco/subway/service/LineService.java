package wooteco.subway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.service.LineServiceRequest;
import wooteco.subway.dto.service.LineServiceResponse;
import wooteco.subway.dto.service.LineUpdateRequest;
import wooteco.subway.dto.service.StationDto;
import wooteco.subway.entity.LineEntity;

@Service
public class LineService {
    private static final String ERROR_MESSAGE_DUPLICATE_NAME = "중복된 지하철 노선 이름입니다.";
    private static final String ERROR_MESSAGE_NOT_EXISTS_ID = "존재하지 않는 지하철 노선 id입니다.";
    private static final String ERROR_MESSAGE_NOT_EXISTS_STATION = "존재하지 않는 역을 지나는 노선은 만들 수 없습니다.";

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final DomainCreatorService domainCreatorService;

    public LineService(LineDao lineDao, SectionDao sectionDao, StationDao stationDao,
        DomainCreatorService domainCreatorService) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.domainCreatorService = domainCreatorService;
    }

    @Transactional
    public LineServiceResponse save(LineServiceRequest lineInfo) {
        String lineName = lineInfo.getName();
        String lineColor = lineInfo.getColor();
        Long upStationId = lineInfo.getUpStationId();
        Long downStationId = lineInfo.getDownStationId();
        int extraFare = lineInfo.getExtraFare();

        validateBeforeSave(lineName, upStationId, downStationId);

        Line lineToAdd = new Line(lineName, lineColor, extraFare);
        LineEntity lineEntity = lineDao.save(lineToAdd);

        saveFirstSection(lineInfo.getDistance(), upStationId, downStationId, lineEntity.getId());

        Line resultLine = domainCreatorService.createLine(lineEntity.getId());

        return new LineServiceResponse(resultLine.getId(), resultLine.getName(), resultLine.getColor(),
            resultLine.getExtraFare(), convertStationToInfo(resultLine.getStations()));
    }

    private void validateBeforeSave(String lineName, Long upStationId, Long downStationId) {
        validateNameDuplication(lineName);
        validateExistStation(upStationId);
        validateExistStation(downStationId);
    }

    private void saveFirstSection(int distance, Long upStationId, Long downStationId, long lineId) {
        Section section = new Section(
                stationDao.getStation(upStationId), stationDao.getStation(downStationId), distance);
        sectionDao.save(lineId, section);
    }

    public List<LineServiceResponse> findAll() {
        List<Line> lines = new ArrayList<>();
        List<LineEntity> lineEntities = lineDao.findAll();
        for (LineEntity lineEntity : lineEntities) {
            lines.add(domainCreatorService.createLine(lineEntity.getId()));
        }

        List<LineServiceResponse> lineServiceResponses = new ArrayList<>();
        for (Line line : lines) {
            lineServiceResponses.add(
                    new LineServiceResponse(
                            line.getId(),
                            line.getName(),
                            line.getColor(),
                            line.getExtraFare(),
                            convertStationToInfo(line.getStations())
                    ));
        }
        return lineServiceResponses;
    }

    public LineServiceResponse find(Long id) {
        validateExists(id);
        LineEntity lineEntity = lineDao.find(id);
        Line line = domainCreatorService.createLine(lineEntity.getId());
        return new LineServiceResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getExtraFare(),
                convertStationToInfo(line.getStations()));
    }

    private List<StationDto> convertStationToInfo(List<Station> stations) {
        return stations.stream()
            .map(station -> new StationDto(station.getId(), station.getName()))
            .collect(Collectors.toList());
    }

    public void update(LineUpdateRequest lineUpdateRequest) {
        Long id = lineUpdateRequest.getId();
        String name = lineUpdateRequest.getName();
        int extraFare = lineUpdateRequest.getExtraFare();

        validateExists(id);
        validateNameDuplication(name);
        Line line = new Line(id, name, lineUpdateRequest.getColor(), extraFare);
        lineDao.update(line);
    }

    @Transactional
    public void delete(Long id) {
        validateExists(id);
        sectionDao.deleteAll(id);
        lineDao.delete(id);
    }

    private void validateExists(Long id) {
        if (!lineDao.existById(id)) {
            throw new NoSuchElementException(ERROR_MESSAGE_NOT_EXISTS_ID);
        }
    }

    private void validateExistStation(Long stationId) {
        if (!stationDao.existById(stationId)) {
            throw new NoSuchElementException(ERROR_MESSAGE_NOT_EXISTS_STATION);
        }
    }

    private void validateNameDuplication(String name) {
        if (lineDao.existByName(name)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_DUPLICATE_NAME);
        }
    }
}
