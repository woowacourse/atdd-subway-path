package wooteco.subway.admin.controller.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;

import wooteco.subway.admin.dto.PathRequest;

@Component
public class PathValidator {
    public void valid(PathRequest pathRequest){
        if(Objects.equals(pathRequest.getSource(), pathRequest.getTarget())){
            throw new IllegalArgumentException("출발역과 도착역이 동일합니다.");
        }
    }
}
