package net.botwithus.api.util.statistics;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class StatisticsHistory {

    private final List<SessionStatistics> sessions;
    private final Map<String, Integer> totalMetrics;
    private Duration totalRuntime;

    /**
     * Constructs a new StatisticsHistory instance with default values.
     */
    public StatisticsHistory() {
        this.sessions = new ArrayList<>();
        this.totalMetrics = new HashMap<>();
        this.totalRuntime = Duration.ZERO;
    }

    /**
     * Adds a new session to the history and updates cumulative metrics.
     *
     * @param session The session to add.
     */
    public void addSession(SessionStatistics session) {
        sessions.add(session);
        session.getMetrics().forEach((metric, value) ->
                totalMetrics.put(metric, totalMetrics.getOrDefault(metric, 0) + value));
        totalRuntime = totalRuntime.plus(session.getRuntime());
    }

    /**
     * Gets the total value of a specific metric.
     *
     * @param metricName The name of the metric.
     * @return The total value of the metric across all sessions.
     */
    public int getTotalMetric(String metricName) {
        return totalMetrics.getOrDefault(metricName, 0);
    }

    /**
     * Gets the total runtime of all sessions combined.
     *
     * @return The total runtime as a Duration.
     */
    public Duration getTotalRuntime() {
        return totalRuntime;
    }

    /**
     * Gets all sessions in the history.
     *
     * @return A list of SessionStatistics.
     */
    public List<SessionStatistics> getSessions() {
        return new ArrayList<>(sessions);
    }

    /**
     * Gets all total metrics as a map.
     *
     * @return A map of metric names to their total values.
     */
    public Map<String, Integer> getTotalMetrics() {
        return new HashMap<>(totalMetrics);
    }

    @Override
    public String toString() {
        return "StatisticsHistory{" +
                "sessions=" + sessions +
                ", totalMetrics=" + totalMetrics +
                ", totalRuntime=" + totalRuntime +
                '}';
    }
}

