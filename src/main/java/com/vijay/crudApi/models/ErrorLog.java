package com.vijay.crudApi.models;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
//@Lombok
@Table(name = "error_logs")
public class ErrorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private String className;

    private String errorCode; // e.g. "LOGIN_FAIL", "MAIL_SEND_ERR", etc.
    

    public ErrorLog() {}

    public ErrorLog(Timestamp createdAt, String message, String className, String errorCode,
            String affectedKey, String affectedValue) {
				this.createdAt = createdAt;
				this.errorMessage = message;
				this.className = className;
				this.errorCode = errorCode;
				this.affectedDataKey = affectedKey;
				this.affectedDataValue = affectedValue;
    		}


    @Column(columnDefinition = "TEXT")
    private String affectedDataKey;

    @Column(columnDefinition = "TEXT")
    private String affectedDataValue;

    private int occurrenceCount = 1;

	/**
	 * @return the createdAt
	 */
	public Timestamp getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the affectedDataKey
	 */
	public String getAffectedDataKey() {
		return affectedDataKey;
	}

	/**
	 * @param affectedDataKey the affectedDataKey to set
	 */
	public void setAffectedDataKey(String affectedDataKey) {
		this.affectedDataKey = affectedDataKey;
	}

	/**
	 * @return the affectedDataValue
	 */
	public String getAffectedDataValue() {
		return affectedDataValue;
	}

	/**
	 * @param affectedDataValue the affectedDataValue to set
	 */
	public void setAffectedDataValue(String affectedDataValue) {
		this.affectedDataValue = affectedDataValue;
	}

	/**
	 * @return the occurrenceCount
	 */
	public int getOccurrenceCount() {
		return occurrenceCount;
	}

	/**
	 * @param occurrenceCount the occurrenceCount to set
	 */
	public void setOccurrenceCount(int occurrenceCount) {
		this.occurrenceCount = occurrenceCount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    // Getters, Setters, Constructors
}
