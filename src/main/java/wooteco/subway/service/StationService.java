package wooteco.subway.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exception.DuplicateStationNameException;
import wooteco.subway.service.dto.request.RequestAssembler;
import wooteco.subway.service.dto.request.StationRequest;
import wooteco.subway.service.dto.response.ResponseAssembler;
import wooteco.subway.service.dto.response.StationResponse;

@Service
@Transactional
public class StationService {

    private final StationRepository stationRepository;
    private final IntegrityValidator integrityValidator;
    private final RequestAssembler requestAssembler;
    private final ResponseAssembler responseAssembler;

    public StationService(StationRepository stationRepository, IntegrityValidator integrityValidator,
                          RequestAssembler requestAssembler, ResponseAssembler responseAssembler) {
        this.stationRepository = stationRepository;
        this.integrityValidator = integrityValidator;
        this.requestAssembler = requestAssembler;
        this.responseAssembler = responseAssembler;
    }

    public StationResponse create(StationRequest stationRequest) {
        validateStationNameNotDuplicated(stationRequest.getName());

        Station temporaryStation = requestAssembler.temporaryStation(stationRequest);
        Station station = stationRepository.save(temporaryStation);
        return responseAssembler.stationResponse(station);
    }

    private void validateStationNameNotDuplicated(String name) {
        if (stationRepository.existsByName(name)) {
            throw new DuplicateStationNameException(name);
        }
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAll() {
        List<Station> stations = stationRepository.getAll();
        return responseAssembler.stationResponses(stations);
    }

    @Transactional(readOnly = true)
    public StationResponse findById(long id) {
        Station station = stationRepository.getById(id);
        return responseAssembler.stationResponse(station);
    }

    public void remove(long id) {
        integrityValidator.validateSectionNotExistByStationId(id);
        stationRepository.remove(id);
    }
}
