package at.htl.api;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Comparator;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        var violation = exception.getConstraintViolations().stream()
                .min(Comparator.comparing(value -> value.getPropertyPath().toString()))
                .orElse(null);
        var error = violation == null
                ? new ErrorResponse("request", "Request validation failed")
                : new ErrorResponse(fieldName(violation), violation.getMessage());
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }

    private String fieldName(ConstraintViolation<?> violation) {
        String field = null;
        for (Path.Node node : violation.getPropertyPath()) {
            if (node.getName() != null) {
                field = node.getName();
            }
        }
        return field == null ? "request" : field;
    }
}
