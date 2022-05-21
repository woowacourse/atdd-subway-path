package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.*;
import wooteco.subway.domain.Path;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.SectionsResponse;
import wooteco.subway.exception.ClientException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    public SectionService(SectionDao sectionDao, LineDao lineDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    @Transactional
    public void save(Long id, SectionRequest request, SectionsResponse response, LineResponse line) {
        validateLineExist(id);
        LineSections lineSections = new LineSections(response.getSections());
        lineSections.validateUpAndDownSameStation(request);
        lineSections.validateSaveCondition(request, line);

        if (lineSections.isAddSectionMiddle(request)) {
            addMiddleSection(id, request, lineSections);
            return;
        }
        sectionDao.save(id, new Section(id, request.getUpStationId(), request.getDownStationId(), request.getDistance()));
    }

    private void addMiddleSection(Long id, SectionRequest request, LineSections lineSections) {
        Optional<Section> targetSection = lineSections.getSections()
                .stream()
                .filter(section -> section.hasSameStationId(request))
                .findAny();

        targetSection.ifPresent(section -> addSectionBySameStationId(id, request, section));
    }

    private void addSectionBySameStationId(Long id, SectionRequest request, Section section) {
        sectionDao.save(id, section.createBySameStationId(id, request));
        sectionDao.delete(id, section);

        section.updateSameStationId(request);
        sectionDao.save(id, section);
    }

    @Transactional
    public void delete(Long id, Long stationId) {
        validateLineExist(id);
        validateStationExist(stationId);
        sectionDao.findById(id).validateDeleteCondition();

        LineSections lineSections = sectionDao.findById(id);
        if (lineSections.isMiddleSection(stationId)) {
            deleteMiddleSection(id, lineSections.findDownSection(stationId).get(), lineSections.findUpSection(stationId).get());
            return;
        }
        deleteEndSection(id, lineSections.findDownSection(stationId), lineSections.findUpSection(stationId));
    }

    private void validateLineExist(Long id) {
        if (!lineDao.isExistById(id)) {
            throw new ClientException("존재하지 않는 노선입니다.");
        }
    }

    private void validateStationExist(Long id) {
        if (!stationDao.isExistById(id)) {
            throw new ClientException("존재하지 않는 역입니다.");
        }
    }

    private void deleteMiddleSection(Long id, Section downSection, Section upSection) {
        sectionDao.save(id, new Section(id, upSection.getUpStationId(), downSection.getDownStationId(),
                upSection.getDistance() + downSection.getDistance()));
        sectionDao.delete(id, downSection);
        sectionDao.delete(id, upSection);
    }

    private void deleteEndSection(Long id, Optional<Section> downSection, Optional<Section> upSection) {
        if (upSection.isEmpty()) {
            sectionDao.delete(id, downSection.get());
        }
        if (downSection.isEmpty()) {
            sectionDao.delete(id, upSection.get());
        }
    }

    public PathResponse findShortestPath(Long source, Long target, Long age) {
        Sections sections = new Sections(sectionDao.findAll());
        Path shortestPath = sections.findShortestPath(source, target, age);
        List<Station> stations = shortestPath.getStationIds()
                .stream()
                .map(stationDao::findById)
                .collect(Collectors.toList());

        return new PathResponse(stations, shortestPath.getDistance(), shortestPath.calculateFare(lineDao.findAll()));
    }
}
