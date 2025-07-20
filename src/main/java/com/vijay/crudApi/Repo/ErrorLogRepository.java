package com.vijay.crudApi.Repo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vijay.crudApi.models.ErrorLog;

@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {

    Optional<ErrorLog> findByClassNameAndErrorCode(String className, String errorCode);
    List<ErrorLog> findByCreatedAtAfter(Timestamp timestamp);

//	Optional<ErrorLog> findByClassNameAndErrorCode(Timestamp date, String errorCode);
}
