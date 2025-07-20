package com.vijay.crudApi.Exception;

import java.sql.Timestamp;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.vijay.crudApi.Repo.ErrorLogService;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ErrorLogService errorLogService;

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        String className = getClassNameFromPath(path);
        String errorCode = "GENERIC_ERR";

        log.error("Exception caught in handler: {}", ex.getMessage());

        errorLogService.logError(
            new Timestamp(System.currentTimeMillis()),      // ✅ timestamp
            className,                                      // ✅ className
            errorCode,                                      // ✅ errorCode
            ex.getMessage(),                                // ✅ message
            Map.of("URL", path)                             // ✅ affectedData map
        );

        return ResponseEntity.status(500).body(Map.of("error", ex.getMessage()));
    }

    private String getClassNameFromPath(String path) {
        if (path.contains("/auth")) return "AuthController";
        if (path.contains("/users")) return "UserController";
        return "Unknown";
    }
}
