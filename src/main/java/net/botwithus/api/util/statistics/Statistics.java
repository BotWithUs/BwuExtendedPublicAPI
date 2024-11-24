package net.botwithus.api.util.statistics;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.botwithus.api.util.statistics.SessionStatistics;
import net.botwithus.api.util.statistics.StatisticsHistory;
import net.botwithus.rs3.game.skills.Skill;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Statistics {

    private final Gson gson = new Gson();
    private final File historyFile;
    private final StatisticsHistory statisticsHistory;
    private Instant startTime;
    private int startingXp;
    private final Map<String, Integer> customCounters = new HashMap<>();
    private final Skill trackedSkill;

    /**
     * Constructs a StatisticsHandler with a specific history file path and skill to track.
     *
     * @param historyFilePath Path to the statistics history file.
     * @param trackedSkill    The skill whose XP to track (optional).
     */
    public StatisticsHandler(String historyFilePath, Skill trackedSkill) {
        this.historyFile = new File(historyFilePath);
        this.trackedSkill = trackedSkill;
        this.startTime = Instant.now();
        this.startingXp = trackedSkill != null ? trackedSkill.getExperience() : 0;
        this.statisticsHistory = loadStatisticsHistory();
    }

    /**
     * Loads the statistics history from the JSON file.
     *
     * @return The loaded StatisticsHistory or a new empty one if the file does not exist.
     */
    private StatisticsHistory loadStatisticsHistory() {
        if (!historyFile.exists()) {
            return new StatisticsHistory();
        }
        try (FileReader reader = new FileReader(historyFile)) {
            Type historyType = new TypeToken<StatisticsHistory>() {}.getType();
            return gson.fromJson(reader, historyType);
        } catch (IOException e) {
            e.printStackTrace();
            return new StatisticsHistory();
        }
    }

    /**
     * Saves the statistics history back to the JSON file.
     *
     * @param history The StatisticsHistory to save.
     */
    private void saveStatisticsHistory(StatisticsHistory history) {
        try (FileWriter writer = new FileWriter(historyFile)) {
            gson.toJson(history, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new session's data to the statistics history.
     *
     * @param sessionStats The session statistics to add.
     */
    public void addNewSession(SessionStatistics sessionStats) {
        StatisticsHistory history = loadStatisticsHistory();
        history.setTotalLogsChopped(history.getTotalLogsChopped() + sessionStats.getLogsChopped());
        history.setTotalXpGained(history.getTotalXpGained() + sessionStats.getXpGained());
        history.setTotalBankingTrips(history.getTotalBankingTrips() + sessionStats.getBankingTrips());
        history.setTotalRuntime(history.getTotalRuntime().plus(sessionStats.getRuntime()));
        history.getSessions().add(sessionStats);
        saveStatisticsHistory(history);
    }

    /**
     * Increments a custom counter by a specified amount.
     *
     * @param counterName The name of the counter.
     * @param amount      The amount to increment by (default is 1).
     */
    public void incrementCounter(String counterName, int amount) {
        customCounters.put(counterName, customCounters.getOrDefault(counterName, 0) + amount);
    }

    /**
     * Increments a custom counter by 1.
     *
     * @param counterName The name of the counter.
     */
    public void incrementCounter(String counterName) {
        incrementCounter(counterName, 1);
    }

    /**
     * Retrieves the value of a custom counter.
     *
     * @param counterName The name of the counter.
     * @return The value of the counter.
     */
    public int getCounterValue(String counterName) {
        return customCounters.getOrDefault(counterName, 0);
    }

    /**
     * Generates a summary of the current session's statistics.
     *
     * @return The current session's statistics.
     */
    public SessionStatistics generateSessionSummary() {
        Instant endTime = Instant.now();
        Duration runtime = Duration.between(startTime, endTime);
        int xpGained = trackedSkill != null ? trackedSkill.getExperience() - startingXp : 0;

        return new SessionStatistics(
                LocalDateTime.now().minus(runtime),
                LocalDateTime.now(),
                getCounterValue("logsChopped"),
                xpGained,
                getCounterValue("bankingTrips"),
                runtime
        );
    }

    /**
     * Finalizes the current session and saves its data.
     */
    public void finalizeSession() {
        SessionStatistics sessionStats = generateSessionSummary();
        addNewSession(sessionStats);
        resetSession();
    }

    /**
     * Resets the session counters and start time for a new session.
     */
    public void resetSession() {
        startTime = Instant.now();
        startingXp = trackedSkill != null ? trackedSkill.getExperience() : 0;
        customCounters.clear();
    }

    /**
     * Retrieves the detailed statistics history.
     *
     * @return The full statistics history.
     */
    public StatisticsHistory getDetailedHistory() {
        return loadStatisticsHistory();
    }
}

