package at.htl.api;

import at.htl.error.ConflictException;
import at.htl.error.DomainException;
import at.htl.error.DuplicateException;
import at.htl.error.InvalidRequestException;
import at.htl.error.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DomainExceptionMapper implements ExceptionMapper<DomainException> {

    @Override
    public Response toResponse(DomainException exception) {
        var status = switch (exception) {
            case NotFoundException ignored -> Response.Status.NOT_FOUND;
            case ConflictException ignored -> Response.Status.CONFLICT;
            case DuplicateException ignored -> Response.Status.CONFLICT;
            case InvalidRequestException ignored -> Response.Status.BAD_REQUEST;
        };
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse(exception.field(), exception.getMessage()))
                .build();
    }
}
