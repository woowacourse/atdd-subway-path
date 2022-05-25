package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.dto.service.StationDto;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationDto save(StationDto stationDto) {
        Station station = new Station(stationDto.getName());
        Station newStation = stationRepository.save(station);
        return new StationDto(newStation.getId(), newStation.getName());
    }

    public List<StationDto> findAll() {
        List<Station> stations = stationRepository.findAll();
        return stations.stream()
            .map(it -> new StationDto(it.getId(), it.getName()))
            .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        stationRepository.delete(id);
    }
}
