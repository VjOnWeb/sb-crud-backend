package com.vijay.crudApi.scheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vijay.crudApi.Repo.ErrorLogRepository;
import com.vijay.crudApi.models.ErrorLog;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class LogMailScheduler {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ErrorLogRepository errorLogRepository;

    private final Dotenv dotenv = Dotenv.load(); // .env loader

    // Runs every 6 hours
    @Scheduled(fixedRate =  6 * 60 * 60 * 1000)
//    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void sendErrorLogs() throws MessagingException, IOException {
    	Instant sixHoursAgo = Instant.now().minus(6, ChronoUnit.HOURS);
    	Timestamp timestamp = Timestamp.from(sixHoursAgo); // ✅ Convert to Timestamp

    	List<ErrorLog> recentLogs = errorLogRepository.findByCreatedAtAfter(timestamp); // ✅ Safe


        if (recentLogs.isEmpty()) return;

        // Create CSV file
        File csvFile = new File("error_logs.csv");
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("ID,ClassName,ErrorCode,ErrorMessage,AffectedKey,AffectedValue,OccurrenceCount,CreatedAt\n");
            for (ErrorLog log : recentLogs) {
                writer.write(String.format(
                    "%d,%s,%s,%s,%s,%s,%d,%s\n",
                    log.getId(),
                    log.getClassName(),
                    log.getErrorCode(),
                    log.getErrorMessage().replace(",", " "),
                    log.getAffectedDataKey(),
                    log.getAffectedDataValue(),
                    log.getOccurrenceCount(),
                    log.getCreatedAt()
                ));
            }
        }

        // Create HTML table
        StringBuilder html = new StringBuilder();
        html.append("<h3>Error Logs (Last 6 Hours)</h3>");
        html.append("<table border='1' cellpadding='5' cellspacing='0'>");
        html.append("<tr><th>ID</th><th>Class</th><th>Error Code</th><th>Message</th><th>Key</th><th>Value</th><th>Count</th><th>Time</th></tr>");

        for (ErrorLog log : recentLogs) {
            html.append("<tr>")
                .append("<td>").append(log.getId()).append("</td>")
                .append("<td>").append(log.getClassName()).append("</td>")
                .append("<td>").append(log.getErrorCode()).append("</td>")
                .append("<td>").append(log.getErrorMessage()).append("</td>")
                .append("<td>").append(log.getAffectedDataKey()).append("</td>")
                .append("<td>").append(log.getAffectedDataValue()).append("</td>")
                .append("<td>").append(log.getOccurrenceCount()).append("</td>")
                .append("<td>").append(log.getCreatedAt()).append("</td>")
                .append("</tr>");
        }

        html.append("</table>");

        // Compose email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // 🔁 Multiple recipients from .env (comma-separated)
        String[] recipients = dotenv.get("ERROR_LOG_EMAIL_TO").split(",");
        helper.setTo(recipients);
        helper.setSubject("Spring Boot Error Logs (Last 6 Hours)");
        helper.setText(html.toString(), true);
        helper.addAttachment("error_logs.csv", csvFile);

        mailSender.send(message);
    }
}
