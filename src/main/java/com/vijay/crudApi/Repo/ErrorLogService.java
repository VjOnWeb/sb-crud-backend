package com.vijay.crudApi.Repo;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vijay.crudApi.models.ErrorLog;

@Service
public class ErrorLogService {

    @Autowired
    private ErrorLogRepository errorLogRepo;

    public void logError(Timestamp timestamp, String className, String errorCode,
                         String message, Map<String, String> affectedData) {

        Optional<ErrorLog> existing = errorLogRepo.findByClassNameAndErrorCode(className, errorCode);

        if (existing.isPresent()) {
            ErrorLog log = existing.get();
            log.setOccurrenceCount(log.getOccurrenceCount() + 1);
            log.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            errorLogRepo.save(log);
        } else {
            ErrorLog newLog = new ErrorLog();
            newLog.setClassName(className);
            newLog.setErrorCode(errorCode);
            newLog.setErrorMessage(message);
            newLog.setCreatedAt(timestamp);

            // Defensive: Check if affectedData has at least one entry
            if (affectedData != null && !affectedData.isEmpty()) {
                Map.Entry<String, String> entry = affectedData.entrySet().iterator().next();
                newLog.setAffectedDataKey(entry.getKey());
                newLog.setAffectedDataValue(entry.getValue());
            }

            errorLogRepo.save(newLog);
        }
    }
}
