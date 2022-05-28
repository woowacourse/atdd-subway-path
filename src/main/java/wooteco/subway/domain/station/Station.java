package wooteco.subway.domain.station;

import wooteco.subway.Validator.Validator;

public class Station {

    private final Long id;
    private final String name;

    public Station(Long id, String name) {
        Validator.checkNull(name);
        this.id = id;
        this.name = name;
    }

    public Station(String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

