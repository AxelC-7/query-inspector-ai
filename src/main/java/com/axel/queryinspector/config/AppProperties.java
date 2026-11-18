package com.axel.queryinspector.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix="app")
public record AppProperties(Database database, Ai ai) {
 public record Database(String defaultSchema, int queryTimeoutSeconds, int maxRows) {}
 public record Ai(Gemini gemini) {}
 public record Gemini(String baseUrl, String apiKey, String model, int timeoutSeconds) {}
}
