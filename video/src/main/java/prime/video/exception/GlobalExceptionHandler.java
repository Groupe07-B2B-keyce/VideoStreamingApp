package prime.video.exception;

import prime.video.model.ProblemDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetails> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(URI.create("https://api.primevideo.com/errors/not-found"));
        problemDetails.setTitle("Resource Not Found");
        problemDetails.setStatus(HttpStatus.NOT_FOUND.value());
        problemDetails.setDetail(ex.getMessage());
        problemDetails.setInstance(URI.create(request.getRequestURI()));
        problemDetails.setCorrelationId(UUID.randomUUID());

        return new ResponseEntity<>(problemDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleGlobalException(Exception ex, HttpServletRequest request) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setType(URI.create("https://api.primevideo.com/errors/internal-server-error"));
        problemDetails.setTitle("Internal Server Error");
        problemDetails.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        problemDetails.setDetail(ex.getMessage());
        problemDetails.setInstance(URI.create(request.getRequestURI()));
        problemDetails.setCorrelationId(UUID.randomUUID());

        return new ResponseEntity<>(problemDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
