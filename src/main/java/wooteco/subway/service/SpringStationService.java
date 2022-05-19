package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.ui.dto.response.StationResponse;

@Transactional
@Service
public class SpringStationService implements StationService {

    private final StationRepository stationRepository;

    public SpringStationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public StationResponse create(String name) {
        Station station = stationRepository.save(new Station(name));
        return DtoAssembler.stationResponse(station);
    }

    @Transactional(readOnly = true)
    @Override
    public List<StationResponse> findAll() {
        List<Station> stations = stationRepository.findAll();
        return DtoAssembler.stationResponses(stations);
    }

    @Override
    public void deleteById(Long id) {
        stationRepository.deleteById(id);
    }
}
