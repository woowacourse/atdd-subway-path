package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineEntity;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.SectionEntity;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.section.creationStrategy.ConcreteCreationStrategy;
import wooteco.subway.domain.section.deletionStrategy.ConcreteDeletionStrategy;
import wooteco.subway.domain.section.sortStrategy.ConcreteSortStrategy;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public LineResponse save(LineRequest lineRequest) {
        LineEntity lineEntity = new LineEntity(lineRequest.getName(), lineRequest.getColor(), lineRequest.getExtraFare());
        checkDuplication(lineEntity.getName());
        LineEntity newLineEntity = lineDao.insert(lineEntity);
        Line line = assembleLine(newLineEntity);

        SectionEntity sectionEntity = new SectionEntity(line.getId(), lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance());
        SectionEntity newSectionEntity = sectionDao.insert(sectionEntity);
        Section section = assembleSection(newSectionEntity);
        line.addSection(section);

        return new LineResponse(line, createStationResponseOf(line));
    }

    private void checkDuplication(String lineName) {
        if (lineDao.existByName(lineName)) {
            throw new IllegalArgumentException("이미 존재하는 노선 이름입니다.");
        }
    }

    public List<LineResponse> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();

        List<Line> lines = lineEntities.stream()
                .map(this::assembleLine)
                .collect(Collectors.toList());

        return lines.stream()
                .map(line -> new LineResponse(line, createStationResponseOf(line)))
                .collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        LineEntity lineEntity = lineDao.findById(id);
        Line line = assembleLine(lineEntity);
        return new LineResponse(line, createStationResponseOf(line));
    }

    public void edit(Long id, String name, String color, int extraFare) {
        lineDao.update(new LineEntity(id, name, color, extraFare));
    }

    public void delete(Long id) {
        lineDao.delete(id);
    }

    private List<StationResponse> createStationResponseOf(Line line) {
        List<Station> sortedStations = line.getStationsInLine();

        return sortedStations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    private Line assembleLine(LineEntity lineEntity) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineEntity.getId());

        List<Section> sectionList = sectionEntities.stream()
                .map(this::assembleSection)
                .collect(Collectors.toList());

        Sections sections = new Sections(sectionList, new ConcreteCreationStrategy(), new ConcreteDeletionStrategy(), new ConcreteSortStrategy());
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), lineEntity.getExtraFare(), sections);
    }

    private Section assembleSection(SectionEntity sectionEntity) {
        Station upStation = stationDao.findById(sectionEntity.getUpStationId());
        Station downStation = stationDao.findById(sectionEntity.getDownStationId());

        return new Section(sectionEntity.getId(), sectionEntity.getLineId(),
                upStation, downStation, sectionEntity.getDistance());
    }

}
