package wooteco.subway.admin.controller.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;

import wooteco.subway.admin.dto.PathRequest;

public class PathValidator {
    public static void valid(PathRequest pathRequest){
        if(Objects.equals(pathRequest.getSource(), pathRequest.getTarget())){
            throw new IllegalArgumentException("출발역과 도착역이 동일합니다.");
        }
    }
}
