package wooteco.subway.admin.dto;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

@Component
public class PathValidator implements ConstraintValidator<PathForm, PathRequest> {
    @Override
    public void initialize(final PathForm constraintAnnotation) {

    }

    @Override
    public boolean isValid(final PathRequest pathRequest,
        final ConstraintValidatorContext context) {
        int invalidCount = 0;

        if (Objects.isNull(pathRequest.getSource())) {
            addConstraintViolation(context, "출발역을 지정해주세요!", "source");
            invalidCount++;
        }

        if (Objects.isNull(pathRequest.getTarget())) {
            addConstraintViolation(context, "도착역을 지정해주세요!", "target");
            invalidCount++;

        }

        if (Objects.isNull(pathRequest.getType())) {
            addConstraintViolation(context, "최단 거리 또는 최단 시간을 클릭해주세요!", "type");
            invalidCount++;

        }

        if (Objects.equals(pathRequest.getSource(), pathRequest.getTarget())) {
            addConstraintViolation(context, "출발역과 도착역이 동일합니다.", "source");
            invalidCount++;
        }

        return invalidCount == 0;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String errorMessage,
        String firstNode, String secondNode, String thirdNode) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage)
            .addPropertyNode(firstNode)
            .addPropertyNode(secondNode)
            .addPropertyNode(thirdNode)
            .addConstraintViolation()
        ;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String errorMessage) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage)
            .addConstraintViolation()
        ;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String errorMessage,
        String firstNode) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage)
            .addPropertyNode(firstNode)
            .addConstraintViolation()
        ;
    }
}
