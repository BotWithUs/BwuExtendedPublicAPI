package net.botwithus.api.util.statistics;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SessionStatistics {

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final Duration runtime;
    private final Map<String, Integer> metrics;

    /**
     * Constructs a new SessionStatistics instance.
     *
     * @param startTime The session's start time.
     * @param endTime   The session's end time.
     * @param metrics   A map of metric names to their values.
     */
    public SessionStatistics(LocalDateTime startTime, LocalDateTime endTime, Map<String, Integer> metrics) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.runtime = Duration.between(startTime, endTime);
        this.metrics = new HashMap<>(metrics);
    }

    /**
     * Gets the session's start time.
     *
     * @return The start time.
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Gets the session's end time.
     *
     * @return The end time.
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Gets the session's runtime.
     *
     * @return The runtime as a Duration.
     */
    public Duration getRuntime() {
        return runtime;
    }

    /**
     * Gets the value of a specific metric.
     *
     * @param metricName The name of the metric.
     * @return The metric's value, or 0 if not found.
     */
    public int getMetric(String metricName) {
        return metrics.getOrDefault(metricName, 0);
    }

    /**
     * Gets all metrics as a map.
     *
     * @return A map of metric names to values.
     */
    public Map<String, Integer> getMetrics() {
        return new HashMap<>(metrics);
    }

    @Override
    public String toString() {
        return "SessionStatistics{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", runtime=" + runtime +
                ", metrics=" + metrics +
                '}';
    }
}

